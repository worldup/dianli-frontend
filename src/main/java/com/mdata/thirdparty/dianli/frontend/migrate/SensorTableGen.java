package com.mdata.thirdparty.dianli.frontend.migrate;

import com.mdata.thirdparty.dianli.frontend.util.ConfigFileParser;
import com.mdata.thirdparty.dianli.frontend.util.conf.SensorsConfiguration;
import com.mdata.thirdparty.dianli.frontend.util.conf.SensorsGuard;
import com.mdata.thirdparty.dianli.frontend.util.conf.SensorsTable;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by administrator on 16/6/5.
 */
public class SensorTableGen {
    public static void migrateSensorGroupAlertConfig(JdbcTemplate jdbcTemplate){
        String createSql="CREATE TABLE IF NOT EXISTS `T_SENSORS_GROUP_ALERT_CONF` (\n" +
                "\t`key` varchar(100) NOT NULL,\n" +
                "\t`group_key` varchar(100),\n" +
                "\t`type` varchar(100),\n" +
                "\t`name` varchar(100),\n" +
                "\t`value` varchar(100),\n" +
                "\tPRIMARY KEY (`key`)\n" +
                ") ";
        jdbcTemplate.batchUpdate(createSql);
        SensorsGuard sensorsGuard=  ConfigFileParser.parse("data/sensorsguard.xml",SensorsGuard.class);

        final  List<SensorsGuard.Guard> guards= sensorsGuard.guards;
        if(guards!=null){
            String insertSql="replace into T_SENSORS_GROUP_ALERT_CONF ( `key`,`group_key`,`type`,`name`,`value`) values(?,?,?,?,?)";

            jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setString(1,guards.get(i).key);
                    ps.setString(2,guards.get(i).nodeKey);
                    ps.setString(3,guards.get(i).type);
                    ps.setString(4,guards.get(i).setting.parameter.name);
                    ps.setString(5,guards.get(i).setting.parameter.value);

                }

                @Override
                public int getBatchSize() {
                    return guards.size();
                }
            });
        }
    }
    public static void migrateSensorGroup(JdbcTemplate jdbcTemplate){
        String createSensorGroupSql="CREATE TABLE  IF NOT EXISTS T_SENSORS_GROUP ( " +
                " `key` varchar(100) NOT NULL, " +
                " `isparent` char(1), " +
                " `name` varchar(200), " +
                " `skey` varchar(100), " +
                " `parentkey` varchar(100), " +
                " PRIMARY KEY (`key`) " +
                ")  ";
        jdbcTemplate.batchUpdate(createSensorGroupSql);

        SensorsConfiguration sensorsConfiguration=ConfigFileParser.parse("data/sensorsconfig.xml",SensorsConfiguration.class);
        List<SensorsConfiguration.Group> groups=sensorsConfiguration.groups;
        if(groups!=null){
            for(SensorsConfiguration.Group group:groups){
                insertGroup(jdbcTemplate,group,"-1");
            }
        }
    }
    private static void insertGroup(JdbcTemplate jdbcTemplate, final SensorsConfiguration.Group group,final String parentKey){
          List<SensorsConfiguration.Group> groups=group.groups;
        String insertSensorGroupSql="replace into T_SENSORS_GROUP ( `key`,`isparent`,`name`,`skey`,`parentkey`) values(?,?,?,?,?)";
        jdbcTemplate.update(insertSensorGroupSql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1,group.key);
                ps.setString(2,"1");
                ps.setString(3,group.name);
                ps.setString(4,"");
                ps.setString(5,parentKey);
            }
        });
        if(groups!=null&&groups.size()>0){
            for(SensorsConfiguration.Group child:groups){
                insertGroup(jdbcTemplate,child,group.key);
            }
        }else{
            final List<SensorsConfiguration.Group.Sensor> sensors=group.sensors;
            if(sensors!=null && sensors.size()>0){

                jdbcTemplate.batchUpdate(insertSensorGroupSql, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1,sensors.get(i).key);
                        ps.setString(2,"0");
                        ps.setString(3,sensors.get(i).name);
                        ps.setString(4,sensors.get(i).skey);
                        ps.setString(5,group.key);
                    }

                    @Override
                    public int getBatchSize() {
                        return sensors.size();
                    }
                });
            }
        }
    }
    public static void migrateSensors(JdbcTemplate jdbcTemplate){
        String createSensorTableSql="CREATE TABLE  IF NOT EXISTS  `T_SENSORS` (\n" +
                "\t`key` varchar(100) NOT NULL,\n" +
                "\t`sid` varchar(100),\n" +
                "\t`name` varchar(200),\n" +
                "\t`type` varchar(100),\n" +
                "\tPRIMARY KEY (`key`)\n" +
                ")  ;";
        String insertSensorTableSql= "replace into T_SENSORS ( `key`,`sid`,`name`,`type`) values(?,?,?,?)";

        String createSensorTypeTableSql="CREATE TABLE IF NOT EXISTS `T_SENSORS_TYPE` (\n" +
                "\t`type` varchar(100) NOT NULL,\n" +
                "\t`name` varchar(200),\n" +
                "\t`lowerValue` varchar(50),\n" +
                "\t`upperValue` varchar(50),\n" +
                "\t`changeValue` varchar(50),\n" +
                "\t`abnormalValue` varchar(50),\n" +
                "\t`updateSpan` varchar(50),\n" +
                "\t`offlineSpan` varchar(50),\n" +
                "\t`adjust` varchar(50),\n" +
                "\t`adjustMultiplier` varchar(50),\n" +
                "\t`adjustOffsetAdder` varchar(50),\n" +
                "\tPRIMARY KEY (`type`)\n" +
                ")  ;";
        String insertSensorTypeTableSql= "replace into T_SENSORS_TYPE ( `type`,`name`,`lowerValue`,`upperValue`" +
                ",changeValue,abnormalValue,updateSpan,offlineSpan,adjust,adjustMultiplier,adjustOffsetAdder) " +
                "values(?,?,?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.batchUpdate(createSensorTableSql,createSensorTypeTableSql);

        SensorsTable sensorsTable=   ConfigFileParser.parse("data/sensorstable.xml",SensorsTable.class);
        final List<SensorsTable.Sensor> sensors= sensorsTable.sensorList;
        if(sensors!=null&&    sensors.size()>0){
            jdbcTemplate.batchUpdate(insertSensorTableSql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setString(1,sensors.get(i).key);
                    ps.setString(2,sensors.get(i).sid);
                    ps.setString(3,sensors.get(i).name);
                    ps.setString(4,sensors.get(i).type);
                }

                @Override
                public int getBatchSize() {
                    return sensors.size();
                }
            });
            jdbcTemplate.batchUpdate(insertSensorTypeTableSql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {

                    ps.setString(1,sensors.get(i).type);
                    ps.setString(2,sensors.get(i).entry.name);
                    ps.setString(3,sensors.get(i).entry.lowerValue);
                    ps.setString(4,sensors.get(i).entry.upperValue);
                    ps.setString(5,sensors.get(i).entry.changeValue);
                    ps.setString(6,sensors.get(i).entry.abnormalValue);
                    ps.setString(7,sensors.get(i).entry.updateSpan);
                    ps.setString(8,sensors.get(i).entry.offlineSpan);
                    ps.setString(9,sensors.get(i).entry.adjust);
                    ps.setString(10,sensors.get(i).entry.adjustMultiplier);
                    ps.setString(11,sensors.get(i).entry.adjustOffsetAdder);
                }

                @Override
                public int getBatchSize() {
                    return sensors.size();
                }
            });

        }


    }
}
