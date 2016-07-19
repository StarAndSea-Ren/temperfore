package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class CodeGen {
    public static void main(String[] args) throws Exception {

        /**
         * 参数1：数据库版本
         * 参数2：自动生成代码默认包
         */
        Schema schema = new Schema(2, "com.yueh.ren.temforecast.model.database");

        entityAdd(schema);

        //获取自动输出代码路径
        //避免团队合作时各人项目路径不同
        String url = CodeGen.class.getResource("/").getFile();
        String[] str = url.split("dbcodegen");
        String outDir = str[0] + "app/src/main/java/";
        System.out.println(outDir);
        new DaoGenerator().generateAll(schema, outDir);
    }

    private static void entityAdd(Schema schema){
        addTemper(schema);
    }

    private static void addTemper(Schema schema){
        Entity temper = schema.addEntity("Temper");
        temper.addStringProperty("time").notNull().primaryKey();
        temper.addDoubleProperty("temperature").notNull();
    }
}
