User:
- Long _id,
- String name,
- String phone

Order
- String phoneNumber,
- String orderNumber,
- String productCode

Product
- String id_,
- String productCode,
- String productName,
- BigDecimal score

COMMANDS:

docker ps  [check all running containers]

docker compose exec mongo mongosh -u root -p pwd --authenticationDatabase admin  [log into mongo shell]

use reactive_paradigm  [select proper db name, which corresponds to defined docker compose file settings]

show collections  [list all existing tables in the db]

db.{collection}.find()  [list all records from mentioned collection]
