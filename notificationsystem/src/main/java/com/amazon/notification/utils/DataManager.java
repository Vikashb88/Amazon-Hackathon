/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amazon.notification.utils;

import com.csvreader.CsvReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

/**
 *
 * @author Vbalu
 */
public class DataManager {

    public static final boolean DEBUG = true;

    protected Sender sender;
    protected Receiver receiver;
    @Autowired
    protected NotificationConfig notificationConfig;

    private static final DataManager singleton = new DataManager();

    protected Jedis jedis;

    public static DataManager getInstance() {

        return singleton;
    }

    private DataManager() {
    }

    public Jedis getEntityManagerFactory() {

        if (jedis == null) {
            sender = new Sender();

            createOutputMap();
        }
        return jedis;
    }

    public void closeEntityManagerFactory() {

        if (jedis != null) {
            jedis.close();

            if (DEBUG) {
                System.out.println("n*** Persistence finished at " + new java.util.Date());
            }
        }
    }

    protected void createOutputMap() {

        jedis = new Jedis("localhost");
        String outputFilePath = "D:\\amazon\\out\\output.csv";
        String susbcriberFilePath = "D:\\amazon\\subscription\\subscription.csv";
        loadData(jedis, outputFilePath);
        loadData(jedis, susbcriberFilePath);
        if (DEBUG) {
            System.out.println("n*** Persistence started at " + new java.util.Date());
        }
    }

    protected void loadData(Jedis jedis, String filePath) {
        try {
            CsvReader reader = new CsvReader(filePath);
            reader.setSkipEmptyRecords(true);
            reader.setTrimWhitespace(true);
            reader.setUseComments(true);
            updateMap(reader, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected String[] getReaderValues(CsvReader csvReader) throws IOException {
        String[] values = csvReader.getValues();
        try {
            while (values.length == 1 && csvReader.readRecord()) {
                values = csvReader.getValues();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return values;
    }

    public Jedis getDataMap() {
        return jedis;
    }

    public boolean updateMap(CsvReader reader, boolean isNewFile) {
        boolean isSuccess = true;
        try {
            while (reader.readRecord()) {
                String[] values = getReaderValues(reader);
                String key = values[0];
                Map<String, String> map;
                if (jedis.exists(key)) {
                    map = jedis.hgetAll(key);

                } else {
                    map = new HashMap();
                }
                if (isNewFile) {
                    checkSubscription(values[1], values[2]);
                }
                pushValueToMap(values, map);

                jedis.hmset(key, map);
            }
        } catch (IOException ex) {
            isSuccess = false;
            Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            reader.close();
            System.out.println("reader is closed");
        }
        return isSuccess;
    }

    public void pushValueToMap(String[] values, Map<String, String> map) {
        if (values.length == 3) {
            map.put(values[1], values[2]);
        } else {
            String value = values[2] + "," + values[3];
            map.put(values[1], value);
        }
    }

    public void checkSubscription(String key, String value) {
        Map<String, String> map = jedis.hgetAll(key);
        checkSubscriptionConditions(map, key, value);

    }

    private static enum ConditionCode {

        EQ, LTE, GT, GTE, LT, LK, UNKNOWN;

        public static ConditionCode typeOf(String s) {
            ConditionCode result = UNKNOWN;
            try {
                result = valueOf(s);
            } catch (Exception e) {

            }
            return result;
        }
    }

    public void checkSubscriptionConditions(Map<String, String> map, String key, String value) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String susbcriber = entry.getKey();
            String[] entryValue = entry.getValue().split(",");
            String condition = entryValue[1];
            String conditionValue = entryValue[0];
            if (sender == null) {
                sender = new Sender();
            }
            switch (ConditionCode.valueOf(condition.toUpperCase())) {
                case EQ:
                    if (key.equals("release date") && compareDates(value, conditionValue) == 0) {
                        sender.sendMessage(susbcriber, key, value);
                    } else if (value.equals(conditionValue)) {

                        sender.sendMessage(susbcriber, key, value);
                    }
                    break;

                case LK:
                    if (value.contains(conditionValue)) {
                        System.out.println("------ Push to Susbcriber-----");
                    }
                    break;
                case LTE:
                    if (key.equals("release date") && compareDates(value, conditionValue) < 0) {
                        sender.sendMessage(susbcriber, key, value);
                    } else if (extractDigits(value) <= extractDigits(conditionValue)) {
                        sender.sendMessage(susbcriber, key, value);
                    }
                    break;
                case GT:
                    if (key.equals("release date") && compareDates(value, conditionValue) > 0) {
                        sender.sendMessage(susbcriber, key, value);
                    } else if (extractDigits(value) > extractDigits(conditionValue)) {
                        sender.sendMessage(susbcriber, key, value);
                    }
                    break;
                case GTE:
                    if (key.equals("release date") && compareDates(value, conditionValue) > 0) {
                        sender.sendMessage(susbcriber, key, value);
                    } else if (extractDigits(value) >= extractDigits(conditionValue)) {
                        sender.sendMessage(susbcriber, key, value);
                    }
                    break;
                case LT:
                    if (key.equals("release date") && compareDates(value, conditionValue) < 0) {
                        sender.sendMessage(susbcriber, key, value);
                    } else if (extractDigits(value) < extractDigits(conditionValue)) {
                        sender.sendMessage(susbcriber, key, value);
                    }
                    break;
                default:
                    break;

            }
        }
    }

    public int compareDates(String dateString1, String dateString2) {
        Date date1 = null, date2 = null;
        try {
            DateFormat format = new SimpleDateFormat("dd-M-yyyy", Locale.ENGLISH);
            date1 = format.parse(dateString1);
            date2 = format.parse(dateString2);
        } catch (Exception e) {

        }
        return date1.compareTo(date2);

    }

    public int extractDigits(String str) {
        String numberOnly = str.replaceAll("[^0-9]", "");
        return Integer.parseInt(numberOnly);
    }

}
