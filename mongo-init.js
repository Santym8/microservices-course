db = db.getSiblingDB('admin');

db.auth("root", "root");

db = db.getSiblingDB('product_service_db');

db.createUser({
    'user': "product_service_user",
    'pwd': "product_service_user",
    'roles': [{
        'role': 'dbOwner',
        'db': 'product_service_db'}]});


