//package com.techcoderz.ruchira.model;
//
//import com.activeandroid.Model;
//import com.activeandroid.annotation.Column;
//import com.activeandroid.annotation.Table;
//import com.activeandroid.query.Delete;
//import com.activeandroid.query.Select;
//
//import java.util.List;
//
///**
// * Created by Shahriar on 9/27/2016.
// */
//
//@Table(name = "outlet")
//public class Outlet extends Model {
//
//    @Column(name = "oid")
//    String oid;
//
//    @Column(name = "title")
//    String title;
//
//    @Column(name = "group")
//    String group;
//
//    public Outlet() {
//        super();
//    }
//
//
//    public static List<Outlet> getAll() {
//        return new Select()
//                .from(Outlet.class)
////                .where("status = 1")
//                .orderBy("title ASC")
//                .execute();
//    }
//
//    public static Outlet getLastRow() {
//        return new Select()
//                .from(Outlet.class)
//                .orderBy("association_id DESC")
//                .executeSingle();
//    }
//
//    public static Outlet getBySId(String cid) {
//        return new Select()
//                .from(Outlet.class)
//                .where("cid = ?", cid)
//                .executeSingle();
//    }
//
//    public static Outlet getByUserName(String cid) {
//        return new Select()
//                .from(Outlet.class)
//                .where("cid = ?", cid)
//                .executeSingle();
//    }
//
//    public static void deleteContacts(String cid) {
//        new Delete().from(Outlet.class)
//                .where("cid = ?", cid)
//                .executeSingle();
//    }
//
//    public static List<Outlet> deleteAll() {
//        return new Select()
//                .from(Outlet.class)
////                .where("status = 1")
////                .orderBy("name ASC")
//                .execute();
//    }
//
//    public String getOid() {
//        return oid;
//    }
//
//    public void setOid(String oid) {
//        this.oid = oid;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getGroup() {
//        return group;
//    }
//
//    public void setGroup(String group) {
//        this.group = group;
//    }
//}
