create table customer (
  id INT not null AUTO_INCREMENT,
  name varchar(30) not null,
  primary key(id)
  );

create table account (
  id INT not null AUTO_INCREMENT,
  acc_name varchar(30) not null,
  customer_id INT not null,
  primary key(id),
  FOREIGN KEY (customer_id)
  REFERENCES customer(id)
    ON DELETE CASCADE
);

create table service_point (
  id INT not null AUTO_INCREMENT,
  sp_name varchar(30) not null,
  sp_type varchar(30) not null,
  account_id INT not null,
  primary key(id),
  FOREIGN KEY (account_id)
  REFERENCES account(id)
    ON DELETE CASCADE
);

insert into customer(name) values("max");
insert into customer(name) values("olesia");
insert into customer(name) values("zorro");

insert into account(acc_name, customer_id) values("acc1", 1);
insert into account(acc_name, customer_id) values("acc2", 1);

select c.name, a.acc_name from customer c
left join account a on a.customer_id = c.id
left join service_point sp on sp.account_id = a.id;


SELECT c.*,
  hex(uamid.multitenant_id) AS ua_uuid,
  ua.util_account_id        AS util_account_id,
  ua.util_account_id_2      AS util_account_id_2,
  sp.id                     AS sp_id,
  sp.meter_type             AS meter_type,
  sp.read_resolution        AS read_resolution
FROM
  (SELECT cs.id, cs.customer_type, cmid.uuid, can.account_number, s.formatted_address FROM customer cs
    INNER JOIN (SELECT customer_id, hex(multitenant_id) AS uuid FROM customer_multitenant_id
    WHERE multitenant_id IN (
      unhex('33F173128DB811E78F01FA888ABE3D2D'),
      unhex('557AF117E0E411E78E7EFA888ABE3D2D'),
      unhex('FD4345580C2E11E88E7EFA888ABE3D2D'),
      unhex('8F9889F00C3311E88E7EFA888ABE3D2D'),
      unhex('AA1776F71D4211E88E7EFA888ABE3D2D')
    )) AS cmid
      ON cmid.customer_id = cs.id
    INNER JOIN site s ON cs.site_id = s.id
    INNER JOIN customer_account_number can ON can.customer_id = cs.id
  WHERE (? IS NULL OR LOWER(s.locality) LIKE ? OR LOWER(s.street_name) LIKE ?)
   ORDER BY s.locality, s.street_name, s.street_number
   LIMIT 10 OFFSET 0 ) AS c
  INNER JOIN utility_acct ua ON c.id = ua.customer_id
  INNER JOIN service_point sp ON ua.service_point_id = sp.id
  LEFT JOIN utility_acct_multitenant_id uamid ON ua.id = uamid.utility_acct_id;



