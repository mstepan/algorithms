drop table if EXISTS RECIPIENT;
create table RECIPIENT (
  id int not null AUTO_INCREMENT,
  first_name varchar(30) not null,
  last_name varchar(30) not null,
  primary key(id),
  constraint recipient_first_last_name_unique UNIQUE (first_name, last_name)
);

drop table if EXISTS CUSTOMER_ORDER;
create table CUSTOMER_ORDER (
  id int not null AUTO_INCREMENT,
  recipient_id int not null,
  description varchar(50),
  primary key(id),
  CONSTRAINT cutomer_order_recipient_fk FOREIGN KEY(recipient_id) REFERENCES RECIPIENT(id)
);

drop table if EXISTS INVOICE;
create table INVOICE (
  id int not null AUTO_INCREMENT,
  recipient_id int not null,
  description varchar(50),
  primary key(id),
  CONSTRAINT invoice_recipient_fk FOREIGN KEY(recipient_id) REFERENCES RECIPIENT(id)
);

drop table if exists DOCTORS;
create table DOCTORS(
  shift_id int not null,
  name varchar(30) not null,
  on_call BOOLEAN not null default false,
  PRIMARY KEY (shift_id, name)
);

INSERT into DOCTORS (shift_id, name, on_call) values (123, 'max', true);
INSERT into DOCTORS (shift_id, name, on_call) values (123, 'olesia', true);
INSERT into DOCTORS (shift_id, name, on_call) values (123, 'zorro', false);