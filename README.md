# Amazon-Hackathon
Large scale configurable notification system

About
    
     

Technologies Used :

      1) Spring 3.x
      2) Redis Server
      3) Apache ActiveMQ 5.10
      
  File Structure :
       We need to have the following folder need to be created by admin, where the files will be processed.
       1) in - input file path
       2) error - while processed files if any error is occured, it will be moved to this folder
       3) archive - after successful process, files will be archived for future reference
       4) subscription - it will have the file with subscribers data.
       
           Here subscription data is maintained in a csv file and then loaded into Redis using java Jedis client. 
           We can have the subscription data in any SQL database and use it query also.
    
    
Subscription Data format :
    
#key,susbcriber,value,condition
title,c.bala1988@gmail.com,Java Basics,eq\n
title,vikash.b88@gmail.com,Test,eq\n
release date,c.bala1988@gmail.com,20-09-2015,lte\n
list price,vikash.b88@gmail.com,5 USD,lte\n

In Subscription CSV we have maintained 6 conditions
  1) eq  - =
  2) lte - <=
  3) lt - <
  4) gt - >
  5) gte - >=
  6) lk - like g 
  
  
#Idea
1. Redis, in-memory datastructure store is used as database. It's a "NoSQL" key-value data store. More precisely, it is a data structure server. 
2. Spring task scheduler will watch the input  folder for n intervals. The intervals are configurable.
3. Once a new file is arrives, using spring batch chunk processing the data are moved to redis. Scalability and parallel processing is handled in Spring batch.
4. It also checks the condition with the subscriber and sends out the notification via email using Active Mq.
5. If the entire file is processed, its moved to the archive folder. For any errors while processing the files, it gets moved to the error folder.
