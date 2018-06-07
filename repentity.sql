SELECT *
FROM (SELECT *
      FROM customer cs INNER JOIN (SELECT
                                     customer_id,
                                     HEX(multitenant_id) AS uuid
                                   FROM customer_multitenant_id
                                   WHERE multitenant_id IN (UNHEX(?))) AS cmid
          ON cmid.customer_id = cs.id
        LEFT JOIN customer_account_number can ON can.customer_id = cs.id
        INNER JOIN site s ON cs.site_id = s.id
        INNER JOIN site_location sl ON s.id = sl.site_id
        LEFT JOIN site_multitenant_id sm ON s.id = sm.site_id

      ORDER BY s.street, s.street_number
      LIMIT 10 OFFSET 0) AS c

  LEFT OUTER JOIN utility_acct ua
    ON c.customer_id = ua.customer_id AND (ua.inactive_date IS NULL OR ua.inactive_date > now())
  LEFT JOIN utility_acct_multitenant_id uamid ON ua.id = uamid.utility_acct_id
  LEFT JOIN service_point sp ON ua.service_point_id = sp.id
  LEFT JOIN service_point_multitenant_id msp ON sp.id = msp.service_point_id
