package com.example.javier.MaterialDesignApp;

//Chứa các thông số quản lý của chương trình
public interface Variables {
    //Đường dẫn 2 server chứa rss
    public static final String[] Server = {"http://theagencytransfer.vn/baomoi/", "http://joomdemo.meximas.com/Android/"};
    //Mảng các báo hiển thị
    public static final String[] PAPERS = {"Tin tổng hợp"};

    public static final int[] sizefont = {18};

    // Tin Tổng hợp
    public static final String[] BAOMOI_CATEGORIES =
            {"Tin Nóng", "Thế Giới", "Xã Hội", "Kinh Tế",
                    "KHCN", "Giáo Dục", "Văn Hóa",
                    "Thể Thao", "Sức Khỏe", "Giải Trí",
                    "Pháp Luật", "Nhà Đất", "Xe 360"};
    public static final String baoMoi = "Baomoi/jsonBaomoi.php?baoid=";
    public static final String[] BAOMOI_LINKS = {
            baoMoi + "TinNong",
            baoMoi + "TheGioi",
            baoMoi + "XaHoi",
            baoMoi + "KinhTe",
            baoMoi + "KHCN",
            baoMoi + "GiaoDuc",
            baoMoi + "VanHoa",
            baoMoi + "TheThao",
            baoMoi + "SucKhoe",
            baoMoi + "GiaiTri",
            baoMoi + "PhapLuat",
            baoMoi + "NhaDat",
            baoMoi + "OtoXemay"};


    // ALL
    public static final String[][] CATEGORIES = {BAOMOI_CATEGORIES};
    public static final String[][] LINKS = {BAOMOI_LINKS};

    public static final String PAPER = "paper";
    public static final String CATEGORY = "category";
    public static final String LINK = "link";

    //HashMap chứa các báo đã xem tạm thời
    //@SuppressLint("UseSparseArrays")
    //public static final HashMap<Integer, List<RssItem>> newsMap = new HashMap<Integer, List<RssItem>>();
    //HashMap chứa page bao da load
    //@SuppressLint("UseSparseArrays")
    //public static final HashMap<Integer, Integer> pageMap = new HashMap<Integer, Integer>();

}
