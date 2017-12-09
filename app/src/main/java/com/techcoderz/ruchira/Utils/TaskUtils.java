package com.techcoderz.ruchira.Utils;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Environment;
import android.util.Log;

import com.techcoderz.ruchira.ModelClasses.Area;
import com.techcoderz.ruchira.ModelClasses.Beat;
import com.techcoderz.ruchira.ModelClasses.Billing;
import com.techcoderz.ruchira.ModelClasses.Company;
import com.techcoderz.ruchira.ModelClasses.Order;
import com.techcoderz.ruchira.ModelClasses.OrderCancelation;
import com.techcoderz.ruchira.ModelClasses.OrderSummary;
import com.techcoderz.ruchira.ModelClasses.Outlet;
import com.techcoderz.ruchira.ModelClasses.OutletRemainning;
import com.techcoderz.ruchira.ModelClasses.ProductCategory;
import com.techcoderz.ruchira.ModelClasses.ProductList;
import com.techcoderz.ruchira.ModelClasses.Promotion;
import com.techcoderz.ruchira.ModelClasses.Report;
import com.techcoderz.ruchira.ModelClasses.Shipping;
import com.techcoderz.ruchira.ModelClasses.Target;
import com.techcoderz.ruchira.ModelClasses.TodayOrder;
import com.techcoderz.ruchira.ModelClasses.TodaySale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Shahriar Workspace on 9/6/2016.
 */
public class TaskUtils {

    final static String TAG = "TaskUtils";
    public static boolean isEmpty(String string) {
        if (string == null) {
            return true;
        }
        if (string.isEmpty()) {
            return true;
        }
        if (string.equals("null")) {
            return true;
        }
        return false;
    }


    public static boolean isNotEmpty(String string) {
        return !TaskUtils.isEmpty(string);
    }


    public static void alertUser(Context context, String message) {
//        TDialogHandler.showDialog(context, null, message);
    }

    public static boolean isFromLocalStorage(String filePath) { ///storage/emulated/0/FcDrawing1454406644872.jpg

        if (filePath == null)
            return false;

        return filePath.contains("storage");
    }

    public static String getAppDirBase() {
        String dirBase = Environment.getExternalStorageDirectory() + File.separator + "Apraise";
        File file = new File(dirBase);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dirBase;
    }

    public static File getNewImageFileForCamera() {
        File root = new File(getAppDirBase() + File.separator + "Camera");
        root.mkdirs();

        File imagePath = new File(root, UUID.randomUUID().toString() + ".jpg");
        Log.d(TAG, "Camera file opened: " + imagePath.getAbsolutePath());

        return imagePath;
    }

    public static void clearUserInfo(Context context) {
        UserPreferences.clearUserInfo(context);
    }

    public static void clearOrderId(Context context) {
        UserPreferences.clearOrderID(context);
    }

    public static void showCurrentDeviceResolutionType(Context context) {

        String deviceScreenResType = "";
        switch (context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                deviceScreenResType = "Large";
                break;

            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                deviceScreenResType = "XLARGE";
                break;

            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                deviceScreenResType = "NORMAL";
                break;

            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                deviceScreenResType = "SMALL";
                break;
        }
        Log.d("Screen Resolution ", deviceScreenResType);
    }


    public static List<TodaySale> setTodayTotalSale(String json) {
        ArrayList<TodaySale> TodayTotalSaleList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonObject2 = jsonObject.getJSONObject("todaySale");
            TodaySale todaySale = new TodaySale();
            todaySale.setYesterdeay(jsonObject2.getString("yesterday"));
            todaySale.setMonthAccumulation(jsonObject2.getString("monthAccumulation"));
            todaySale.setMonthDailyAvg(jsonObject2.getString("monthDailyAvg"));
            todaySale.setMonthWeeklyAvg(jsonObject2.getString("monthWeeklyAvg"));
            todaySale.setTodaySale(jsonObject2.getString("today"));
            TodayTotalSaleList.add(todaySale);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return TodayTotalSaleList;
    }

    public static List<TodayOrder> setTodayOrder(String json) {
        ArrayList<TodayOrder> TodayTotalOrderList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonObject2 = jsonObject.getJSONObject("todayOrder");
            TodayOrder todayOrder = new TodayOrder();
            todayOrder.setYesterdeay(jsonObject2.getString("yesterday"));
            todayOrder.setMonthAccumulation(jsonObject2.getString("monthAccumulation"));
            todayOrder.setMonthDailyAvg(jsonObject2.getString("monthDailyAvg"));
            todayOrder.setMonthWeeklyAvg(jsonObject2.getString("monthWeeklyAvg"));
            todayOrder.setTodayOrder(jsonObject2.getString("today"));
            TodayTotalOrderList.add(todayOrder);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return TodayTotalOrderList;
    }

    public static List<Target> setTarget(String json) {
        ArrayList<Target> TodayTotalOrderList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonObject2 = jsonObject.getJSONObject("target");

//            for (int i = 0; i < jsonObject2.length(); i++) {
            Target todayOrder = new Target();
            todayOrder.setThisMonth(jsonObject2.getString("thisMonth"));
            todayOrder.setCurrentAchive(jsonObject2.getString("currentAchieve"));
            todayOrder.setRemainningTarget(jsonObject2.getString("remainingTarget"));
            todayOrder.setAvgTargetVisit(jsonObject2.getString("avgTargetVisit"));
            todayOrder.setRemainingVisit(jsonObject2.getString("remainingVisit"));
            TodayTotalOrderList.add(todayOrder);
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return TodayTotalOrderList;
    }

    public static List<ProductCategory> setProductCategory(String json) {
        ArrayList<ProductCategory> productCategoryList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("productCategory");
            for (int i = 0; i < jsonArray.length(); i++) {
                ProductCategory bannerImage = new ProductCategory();
                bannerImage.setId(jsonArray.getJSONObject(i).getString("id"));
                bannerImage.setName(jsonArray.getJSONObject(i).getString("name"));
                productCategoryList.add(bannerImage);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return productCategoryList;
    }

    public static List<OrderCancelation> setOrderCancelation(String json) {
        ArrayList<OrderCancelation> cancelationList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("reason");
            for (int i = 0; i < jsonArray.length(); i++) {
                OrderCancelation orderCancelation = new OrderCancelation();
                orderCancelation.setCancelationId(jsonArray.getJSONObject(i).getString("id"));
                orderCancelation.setTitle(jsonArray.getJSONObject(i).getString("reasons"));
                cancelationList.add(orderCancelation);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return cancelationList;
    }

    public static List<Beat> setBeat(String json) {
        ArrayList<Beat> bannerImageList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("beat");
            for (int i = 0; i < jsonArray.length(); i++) {
                Beat bannerImage = new Beat();
                bannerImage.setId(jsonArray.getJSONObject(i).getString("id"));
                bannerImage.setTitle(jsonArray.getJSONObject(i).getString("title"));
                bannerImageList.add(bannerImage);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return bannerImageList;
    }

    public static List<Company> setCompany(String json) {
        ArrayList<Company> companyList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONArray jsonArray = jsonObject.getJSONArray("company");

            for (int i = 0; i < jsonArray.length(); i++) {
                Company company = new Company();
                company.setCompanyId(jsonArray.getJSONObject(i).getString("companyId"));
                company.setCompanyName(jsonArray.getJSONObject(i).getString("name"));
                companyList.add(company);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return companyList;
    }

    public static List<OrderSummary> setOrderSummaryList(String json) {
        ArrayList<OrderSummary> orderSummaryList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONArray jsonArray = jsonObject.getJSONArray("order");

            for (int i = 0; i < jsonArray.length(); i++) {
                OrderSummary orderSummary = new OrderSummary();
                orderSummary.setOrderDate(jsonArray.getJSONObject(i).getString("orderDate"));
                orderSummary.setInvoiceNo(jsonArray.getJSONObject(i).getString("invoiceNo"));
                orderSummary.setAmount(jsonArray.getJSONObject(i).getString("amount"));
                orderSummaryList.add(orderSummary);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return orderSummaryList;
    }


    public static List<Order> setOrderListDetails(String json) {
        ArrayList<Order> orderList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONArray jsonArray = jsonObject.getJSONArray("orderList");

            for (int i = 0; i < jsonArray.length(); i++) {
                Order order = new Order();
                order.setProductName(jsonArray.getJSONObject(i).getString("productName"));
                order.setProductName(jsonArray.getJSONObject(i).getString("productName"));
                order.setCost(jsonArray.getJSONObject(i).getString("cost"));
                order.setQuantity(jsonArray.getJSONObject(i).getString("quanity"));
                orderList.add(order);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return orderList;
    }

    public static List<Order> setOrderList(String json) {
        ArrayList<Order> orderList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("orderList");
            for (int i = 0; i < jsonArray.length(); i++) {
                Order order = new Order();
                order.setProductName(jsonArray.getJSONObject(i).getString("productName"));
                order.setQuantity(jsonArray.getJSONObject(i).getString("productQuantity"));
                order.setCost(jsonArray.getJSONObject(i).getString("cost"));
                orderList.add(order);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return orderList;
    }

    public static List<Shipping> setShippingList(String json) {
        ArrayList<Shipping> shippingList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonObject2 = jsonObject.getJSONObject("shipping");
            Shipping shipping = new Shipping();
            shipping.setAddress(jsonObject2.getString("address"));
            shipping.setCity(jsonObject2.getString("city"));
            shipping.setEmail(jsonObject2.getString("email"));
            shipping.setPhone(jsonObject2.getString("phone"));
            shipping.setPostcode(jsonObject2.getString("postcode"));
            shipping.setTo(jsonObject2.getString("to"));
            shippingList.add(shipping);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return shippingList;
    }

    public static List<Promotion> setProductPromotion(String json, int a) {
        ArrayList<Promotion> promotionList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("promtion");
            if (a == 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    Promotion promotion = new Promotion();
                    promotion.setPromotionId(jsonArray.getJSONObject(i).getString("promotionId"));
                    promotion.setProductName(jsonArray.getJSONObject(i).getString("productName"));
                    promotion.setTitle(jsonArray.getJSONObject(i).getString("promotionTitle"));
                    promotion.setPromotionStartDate(jsonArray.getJSONObject(i).getString("promotionStartDate"));
                    promotion.setPromotionEndDate(jsonArray.getJSONObject(i).getString("promotionEndDate"));
                    promotionList.add(promotion);
                }
            } else {
                for (int i = 0; i < jsonArray.length(); i++) {
                    Promotion promotion = new Promotion();
                    promotion.setPromotionId(jsonArray.getJSONObject(i).getString("promotionId"));
                    promotion.setTitle(jsonArray.getJSONObject(i).getString("promotionTitle"));
                    promotion.setPromotionStartDate(jsonArray.getJSONObject(i).getString("promotionStartDate"));
                    promotion.setPromotionEndDate(jsonArray.getJSONObject(i).getString("promotionEndDate"));
                    promotionList.add(promotion);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return promotionList;
    }

    public static List<Billing> setBillingList(String json) {
        ArrayList<Billing> billingList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonObject2 = jsonObject.getJSONObject("billing");
            Billing billing = new Billing();
            billing.setAddress(jsonObject2.getString("address"));
            billing.setCity(jsonObject2.getString("city"));
            billing.setEmail(jsonObject2.getString("email"));
            billing.setPhone(jsonObject2.getString("phone"));
            billing.setPostcode(jsonObject2.getString("postcode"));
            billing.setTo(jsonObject2.getString("to"));
            billingList.add(billing);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return billingList;
    }

    public static List<Promotion> setPromotion(String json) {
        ArrayList<Promotion> promotionList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONArray jsonArray = jsonObject.getJSONArray("promotion");

            for (int i = 0; i < jsonArray.length(); i++) {
                Promotion promotion = new Promotion();
                promotion.setPromotionId(jsonArray.getJSONObject(i).getString("id"));
                promotion.setTitle(jsonArray.getJSONObject(i).getString("title"));
                promotionList.add(promotion);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return promotionList;
    }

    public static List<Report> setYearlyReport(String json) {
        ArrayList<Report> reportList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONArray jsonArray = jsonObject.getJSONArray("report");

            for (int i = 0; i < jsonArray.length(); i++) {
                Report report = new Report();
                report.setMonth(jsonArray.getJSONObject(i).getString("month"));
                report.setOrrder(jsonArray.getJSONObject(i).getString("orders"));
                report.setAmmount(jsonArray.getJSONObject(i).getString("amount"));
                reportList.add(report);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reportList;
    }

    public static List<Report> setTodayReport(String json) {
        ArrayList<Report> reportList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("report");

            for (int i = 0; i < jsonArray.length(); i++) {
                Report report = new Report();
                report.setMonth(jsonArray.getJSONObject(i).getString("productName"));
                report.setOrrder(jsonArray.getJSONObject(i).getString("quantity"));
                report.setAmmount(jsonArray.getJSONObject(i).getString("amount"));
                reportList.add(report);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reportList;
    }

    public static List<Area> setArea(String json) {
        ArrayList<Area> areaList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONArray jsonArray = jsonObject.getJSONArray("area");

            for (int i = 0; i < jsonArray.length(); i++) {
                Area area = new Area();
                area.setBeatId(jsonArray.getJSONObject(i).getString("beatId"));
                area.setBeatName(jsonArray.getJSONObject(i).getString("beatName"));
                areaList.add(area);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return areaList;
    }

    public static List<Report> setMonthlyReport(String json) {
        ArrayList<Report> reportList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONArray jsonArray = jsonObject.getJSONArray("report");

            for (int i = 0; i < jsonArray.length(); i++) {
                Report report = new Report();
                report.setMonth(jsonArray.getJSONObject(i).getString("date"));
                report.setOrrder(jsonArray.getJSONObject(i).getString("orders"));
                report.setAmmount(jsonArray.getJSONObject(i).getString("amount"));
                reportList.add(report);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reportList;
    }


    public static List<ProductList> setProductList(String json) {
        ArrayList<ProductList> productList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("product");
            for (int i = 0; i < jsonArray.length(); i++) {
                ProductList product = new ProductList();
                product.setProductId(jsonArray.getJSONObject(i).getString("productId"));
                product.setProductName(jsonArray.getJSONObject(i).getString("productName"));
                product.setProductSku(jsonArray.getJSONObject(i).getString("productSku"));
                product.setFlag(jsonArray.getJSONObject(i).getString("flag"));
                product.setPromotionId(jsonArray.getJSONObject(i).getString("promotionId"));

                product.setPricePerCarton(jsonArray.getJSONObject(i).getInt("pricePerCarton"));
                product.setPricePerPiece(jsonArray.getJSONObject(i).getInt("pricePerPiece"));
                productList.add(product);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return productList;
    }

    public static List<Outlet> setOutlet(String json) {
        ArrayList<Outlet> outletList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("outlet");
            for (int i = 0; i < jsonArray.length(); i++) {
                Outlet outlet = new Outlet();
                outlet.setOutletId(jsonArray.getJSONObject(i).getString("id"));
                outlet.setTitle(jsonArray.getJSONObject(i).getString("title"));
                outlet.setGroup(jsonArray.getJSONObject(i).getString("group"));
                outlet.setFlag(jsonArray.getJSONObject(i).getString("flag"));
                outlet.setReason(jsonArray.getJSONObject(i).getString("reason"));
                outlet.setOid(jsonArray.getJSONObject(i).getString("existOrderId"));
                outletList.add(outlet);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return outletList;
    }

    public static List<OutletRemainning> setOutletRemainning(String json) {
        ArrayList<OutletRemainning> TodayTotalOrderList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonObject2 = jsonObject.getJSONObject("outletRemaining");
            OutletRemainning todayOrder = new OutletRemainning();
            todayOrder.setTotalOutlet(jsonObject2.getString("totalOutlet"));
            todayOrder.setOutletVisited(jsonObject2.getString("outletVisited"));
            todayOrder.setOutletRemained(jsonObject2.getString("outletRemained"));
            todayOrder.setAchieveInPercent(jsonObject2.getString("achieveInPercent"));
            todayOrder.setOutletAchieve(jsonObject2.getString("outletAchieve"));
            TodayTotalOrderList.add(todayOrder);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return TodayTotalOrderList;
    }


}
