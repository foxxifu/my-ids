DROP TRIGGER IF EXISTS dump_realtimeH_after_insert_realtimeT;
CREATE TRIGGER dump_realtimeH_after_insert_realtimeT 
AFTER INSERT ON ids_station_realtime_data_t FOR EACH ROW 
INSERT INTO ids_station_realtime_data_h SELECT
	collect_time,
	station_code,
	enterprise_id,
	radiation_intensity,
	product_power,
	day_cap,
	active_power,
	day_income,
	total_income
FROM
	ids_station_realtime_data_t
WHERE
	station_code = new.station_code
AND collect_time < new.collect_time;


