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
       4) subscription - it will have the file with subcription data.
       
           Here subscription data is maintained in a csv file and then loaded into Redis using java Jedis client. We can have the 
    subscription data in any SQL database and use it query also.
    
    Subscription Data format :
    
#key,susbcriber,value,condition
title,c.bala1988@gmail.com,Java Basics,eq
title,vikash.b88@gmail.com,Test,eq
release date,c.bala1988@gmail.com,20-09-2015,lte
list price,vikash.b88@gmail.com,5 USD,lte

In Subscription CSV we have maintained 6 conditions
  1) eq  - =
  2) lte - <=
  3) lt - <
  4) gt - >
  5) gte - >=
  6) lk - like 
      

