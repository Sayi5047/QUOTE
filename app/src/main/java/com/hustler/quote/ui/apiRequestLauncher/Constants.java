package com.hustler.quote.ui.apiRequestLauncher;

import android.graphics.Color;

/**
 * Created by Sayi on 07-10-2017.
 */

public class Constants {
    public static final String API_TOKEN = "Token token=28e954724c62237845fec852bc2b15c7";
    public static final String API_IMAGES_TOKEN = "7732312-2d2304a57655fc65d52bde5f1";
    public static final String UNSPLASH_APP_ID = "52c825a0d098b74e51389bf4d6e1e94c5b37f1a8232918b576f69cb2931c806c";
    public static final String UNSPLASH_CLIENT_ID = "1f3500dc54aacab31fab3b357f463479adcfd856fbcb559f37e0e755f46da7e6";


    public static final String API_FAVQ_RANDOM_QUOTES = "https://favqs.com/api/quotes/";
    public static final String API_FAVQ_FILTER_QUOTES = "https://favqs.com/api/quotes/?filter=";
    public static final String API_FAVQ_FLTER_QUOTES = "https://favqs.com/api/quotes/:";

    public static final String PER_PAGE = "&per_page=30";
    public static final String QUERY = "&query=";
    public static final String PAGE_NUMBER = "&page=";

    public static final String API_GET_IMAGES_FROM_PIXABAY = "https://pixabay.com/api/?key=" + API_IMAGES_TOKEN;
    public static final String API_GET_IMAGES_FROM_UNSPLASH = "https://api.unsplash.com/photos?client_id=" + UNSPLASH_CLIENT_ID + PER_PAGE;
    public static final String API_GET_Collections_FROM_UNSPLASH = "https://api.unsplash.com/search/photos?client_id=" + UNSPLASH_CLIENT_ID;


    public static final String FONT_NEVIS = "fonts/nevis.ttf";
    public static final String FONT_ZINGCURSIVE = "fonts/zingcursive.otf";
    public static final String FONT_multicolore = "fonts/multicolore.otf";
    public static final String FONT_Sans_Bold = "fonts/sans_bold.ttf";
    public static final String FONT_Roboto_regular = "fonts/robot_regular.ttf";
    public static final String FONT_Google_sans_regular = "fonts/google_sans_reg.ttf";
    public static final String FONT_CIRCULAR = "fonts/cstd_reg.otf";


    public static final String INTENT_QUOTE_OBJECT_KEY = "QUOTE_OBJECT_TRAVELLER_KEY";
    public static final String BUNDLE_OBJECT = "bundle_k";
    public static final int DEFAULT_COLOR = Color.RED;
    public static final String TEXT = "Text_Module";
    public static final String BG = "Bg_Module";
    public static final String JPEG = "JPEG";
    public static final String PNG = "PNG";
    public static final String IS_DB_LOADED_PREFERENCE = "is_db_loaded_key";
    public static final String IS_USER_SAW_INRODUCTION = "is_user_saw_intro_key";
    public static final String SAHRED_PREFS_DEVICE_HEIGHT_KEY = "shared_prefs_device_height";
    public static final String APPFOLDER = "Quotzy";
    public static final String INTENT_UNSPLASH_IMAGE_FOR_EDIOTR_KEY = "Intent_unsplash_image_editor_key";
    public static String INTENT_IS_FROM_EDIT_KEY = "is_From_Edit_Activity_intent_key";

    public static final String CATEGORIES_STRING =


            "Age\n" +
                    "Alone\n" +
                    "Anger\n" +
                    "Architecture\n" +
                    "Art\n" +
                    "Attitude\n" +
                    "Beauty\n" +

                    "Belief\n" +
                    "Bible\n" +
                    "Books\n" +
                    "Brave\n" +
                    "Business\n" +
                    "Cats\n" +
//                    DONE
                    "Chance\n" +
                    "Change\n" +
                    "Communication\n" +
                    "Computers\n" +
                    "Creativity\n" +
                    "Dignity\n" +

                    "Duty\n" +
                    "Death\n" +
                    "Design\n" +
                    "Diet\n" +
                    "Dreams\n" +
                    "Effort\n" +

                    "Education\n" +
                    "Environmental\n" +
                    "Equality\n" +
                    "Experience\n" +
                    "Failure\n" +
                    "Faith\n" +

                    "Family\n" +
                    "Father\n" +
                    "Fear\n" +
                    "Food\n" +
                    "Forgive\n" +
                    "Freedom\n" +

                    "Friendship\n" +
                    "Future\n" +
                    "Gardening\n" +
                    "God\n" +
                    "Good\n" +
                    "Government\n" +

                    "Gratitude\n" +
                    "Great\n" +
                    "Happiness\n" +
                    "Health\n" +
                    "History\n" +
                    "Home\n" +

                    "Hope\n" +
                    "Humor\n" +
                    "Imagination\n" +
                    "Independence\n" +
                    "Inspirational\n" +
                    "Intelligence\n" +

                    "Joy\n" +
                    "Knowledge\n" +
                    "Kindness\n" +
                    "Leadership\n" +
                    "Learning\n" +
                    "Legacy\n" +

                    "Life Favorite\n" +
                    "Love Favorite\n" +
                    "Marriage\n" +
                    "Medical\n" +
                    "Memory\n" +
                    "Men\n" +

                    "Mom\n" +
                    "Money\n" +
                    "Mercy\n" +
                    "Motivational \n" +
                    "Movies\n" +
                    "Music\n" +

                    "Nature\n" +
                    "New Year's\n" +
                    "Parenting\n" +
                    "Patience\n" +
                    "Patriotism\n" +
                    "Peace\n" +

                    "Poetry\n" +
                    "Politics\n" +
                    "Potential\n" +
                    "Power\n" +
                    "Reputation\n" +

                    "Religion\n" +
                    "Respect\n" +
                    "Romantic\n" +
                    "Sad\n" +
                    "Saint Patrick's Day\n" +
                    "Science\n" +

                    "Smile\n" +
                    "Society\n" +
                    "Sports\n" +
                    "Strength\n" +
                    "Success \n" +


                    "Sympathy\n" +
                    "Teacher\n" +
                    "Thankful\n" +

                    "Time\n" +
                    "Trust\n" +
                    "Truth\n" +

                    "War\n" +
                    "Wisdom \n" +
                    "Women\n" + "Work\n" +
                    "Youth\n";


    //ORIGINAL
    public static final String ADS_APP_ID = "ca-app-pub-7219349290716348~4164465126";

    //    DUPE
    public static final String TEST_SMART_BANNER_AD = "ca-app-pub-3940256099942544/6300978111";
    public static final String TEST_INTERSTITIAL_AD = "ca-app-pub-3940256099942544/1033173712";
    public static final String TEST_REWARDED_AD = "ca-app-pub-3940256099942544/5224354917";


    public static final int WIDTH_DEFAULT = 100;
    public static final int WIDTH_MAX = 1000;
    public static final int WIDTH_MIN = 0;
    public static final int WEIGHT_DEFAULT = 400;
    public static final int WEIGHT_MAX = 1000;
    public static final int WEIGHT_MIN = 0;
    public static final float ITALIC_DEFAULT = 0f;
    public static final float ITALIC_MAX = 1f;
    public static final float ITALIC_MIN = 0f;


    //    INTENT KEYS
    public static String TEMP_FILE_NAME_KEY = "File_temporary_name_key";
    public static String FONTS_LOCATION = "File_temporary_name_key";
    public static String FONTS_TEMP_LOCATIONS = "File_temporary_name_key";
    public static final String COLOUR_KEY = "colour_key";
    public static String Widget_current_object = "WIDGET_CURRENT_OBJECT";
    public static String Pager_position = "PAGER_POSITION_KEY";
    public static String PAGER_LIST_WALL_OBKHECTS = "PAGER_LIST_OBJECTS_KEY";
    public static String Shared_prefs_image_resol_key = "Shared_prefs_image_res_key";
    public static String ImageUrl_to_download = "IMAGE_TO_DOWNLOAD_KEY";
    public static String Wallpapers = "WallPapers";
    public static String Image_Name_to_save_key = "Intent_wallpaper_name_key";
    public static String is_to_setWallpaper_fromActivity = "is_to_set_wallpaper_from_activity";
    public static String is_from_fav = "IS_FROM_FAVOURITE";
    public static String Shared_prefs_Images_loaded_for_first_time = "shared_prefs_image_loaded_-for_first_key";
    public static String Shared_prefs_loaded_images = "shared_prefs_images_loaded_key";
    public static String Shared_prefs_loaded_images_for_service_key = "shared_prefs_images_loaded_for_service_key";
    public static String Shared_prefs_images_loaded_times = "shared_prefs_images_loaded_times_key";
    public static String Shared_prefs_current_service_image_key = "shared_prefs_current_image_key";
    public static String Shared_prefs_current_service_image_Size_key = "shared_prefs_current_images_size_key";
    public static String ALARM_INTENT__IS_DOWNLOAD_INTENT_FLAG="is_download_intent_alarm_key";


//    UNSPLASH IDS

}
