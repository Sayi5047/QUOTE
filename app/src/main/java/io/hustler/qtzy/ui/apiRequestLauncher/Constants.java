package io.hustler.qtzy.ui.apiRequestLauncher;

import android.graphics.Color;
import android.os.Environment;

import java.io.File;

/**
 * Created by Sayi on 07-10-2017.
 */
/*   Copyright [2018] [Sayi Manoj Sugavasi]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.*/
public final class Constants {
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


    public static final String FONT_ZINGCURSIVE = "fonts/zingcursive.otf";
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
    public static final String HOME_SCREEN_NUMBER = "home_screen_number_object";
    public static final String APP_SAVED_PICTURES_FOLDER = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "Quotzy";
    public static final String APP_WALLPAPERS_FOLDER = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "QuotzyWallPapers";
    public static final String PREVIOUS_COLOR = "previous_color";
    public static String INTENT_IS_FROM_EDIT_KEY = "is_From_Edit_Activity_intent_key";


    public static final String CATEGORIES_STRING = "Alone\n" + "Angry\n" + "Architecture\n" + "Art\n" + "Attitude\n" + "Beauty\n" + "Belief\n" + "Bible\n" + "Books\n" + "Brave\n" + "Business\n" + "Cats\n" +


            "Change\n" + "Character\n" + "Communication\n" + "Computers\n" + "Creativity\n" +

            "Dignity\n" + "Duty\n" + "Death\n" + "Design\n" + "Diet\n" + "Dreams\n" +

            "Effort\n" + "Education\n" + "Environmental\n" + "Equality\n" + "Experience\n" +

            "Failure\n" + "Faith\n" + "Family\n" + "Father\n" + "Fear\n" + "Food\n" + "Forgiveness\n" + "Freedom\n" + "Friendship\n" + "Future\n" +

            "Good\n" + "Government\n" + "Gratitude\n" + "Great\n" +

            "Happiness\n" + "Health\n" + "History\n" + "Home\n" + "Hope\n" + "Humor\n" +

            "Imagination\n" + "Independence\n" + "Inspirational\n" + "Intelligence\n" +

            "Joy\n" +

            "Knowledge\n" + "Kindness\n" +

            "Leadership\n" + "Learning\n" + "Legacy\n" + "Life \n" + "Love \n" +

            "Marriage\n" + "Medical\n" + "Memory\n" + "Men\n" + "Mom\n" + "Money\n" + "Mercy\n" + "Motivational \n" + "Morals\n" + "Music\n" +

            "Nature\n" + "New Year\n" +

            "Parenting\n" + "Patience\n" + "Patriotism\n" + "Peace\n" + "Poetry\n" + "Politics\n" + "Potential\n" + "Power\n" +

            "Reputation\n" + "Religion\n" + "Respect\n" + "Romantic\n" +

            "Sad\n" + "Sex\n" + "Science\n" + "Smile\n" + "Society\n" + "Sports\n" + "Strength\n" + "Success \n" + "Sympathy\n" +

            "Teacher\n" + "Thankful\n" + "Time\n" + "Trust\n" + "Truth\n" +

            "War\n" + "Wisdom\n" + "Women\n" + "Work\n" +

            "Youth\n"
//                    DONE
            ;


    //ORIGINAL
    public static final String ADS_APP_ID = "ca-app-pub-9653011938091807~6264394365";

    //    DUPE
    public static final String TEST_SMART_BANNER_AD = "ca-app-pub-9653011938091807/8683595028";
    public static final String TEST_INTERSTITIAL_AD = "ca-app-pub-9653011938091807/3150683855";
    public static final String TEST_REWARDED_AD = "ca-app-pub-9653011938091807/7917308260";


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
    public static String ALARM_INTENT__IS_DOWNLOAD_INTENT_FLAG = "is_download_intent_alarm_key";
    public static String IS_QUOTES_LOADED_KEY = "is_quotes_loaded_to_db_key";
    public static String INTRO_KEY_IS_VIEWPAGER_COMPLETED = "VIEW_PAGER_INTRO_COMPLETETION_KEY";

    /*END POINTS*/
    public static final String END_POINT = "http://192.168.43.205:8080";
    /*PATHS FOR FINDING METHODS*/
    public static final String PATH_QUOTES = END_POINT + "/private/mobile/quotes/";
    public static final String PATH_AUTH = END_POINT + "/private/onBoard/v0/";
    /*METHOD NAMES*/
    public static final String QUOTZY_API_GET_QUOTES_BY_CATEGORY = PATH_QUOTES + "getAllQuotesByCategory/";
    public static final String QUOTZY_API_EMAIL_SIGNUP_USER = PATH_AUTH + "emailSignUpUser/";
    public static final String QUOTZY_API_GOOGLE_SIGNUP_USER = PATH_AUTH + "gSignup/";
    public static final String QUOTZY_API_GOOGLE_LOGIN_USER = PATH_AUTH + "loginUser/";
//    UNSPLASH IDS

    public static final String offlineQuote = " {\"data\":[\n" + "  {\n" + "      \"quoteAuthor\": \"Thomas Edison\",\n" + "      \"quoteText\": \"Genius is one percent inspiration and ninety-nine percent perspiration.\"\n" + "  },\n" + "  {\n" + "      \"quoteAuthor\": \"Yogi Berra\",\n" + "      \"quoteText\": \"You can observe a lot just by watching.\"\n" + "  },\n" + "  {\n" + "      \"quoteAuthor\": \"Abraham Lincoln\",\n" + "      \"quoteText\": \"A house divided against itself cannot stand.\"\n" + "  },\n" + "  {\n" + "      \"quoteAuthor\": \"Johann Wolfgang von Goethe\",\n" + "      \"quoteText\": \"Difficulties increase the nearer we get to the goal.\"\n" + "  },\n" + "  {\n" + "      \"quoteAuthor\": \"Byron Pulsifer\",\n" + "      \"quoteText\": \"Fate is in your hands and no one elses\"\n" + "  },\n" + "  {\n" + "      \"quoteAuthor\": \"Lao Tzu\",\n" + "      \"quoteText\": \"Be the chief but never the lord.\"\n" + "  },\n" + "  {\n" + "      \"quoteAuthor\": \"Carl Sandburg\",\n" + "      \"quoteText\": \"Nothing happens unless first we dream.\"\n" + "  },\n" + "  {\n" + "      \"quoteAuthor\": \"Aristotle\",\n" + "      \"quoteText\": \"Well begun is half done.\"\n" + "  },\n" + "  {\n" + "      \"quoteAuthor\": \"Yogi Berra\",\n" + "      \"quoteText\": \"Life is a learning experience, only if you learn.\"\n" + "  },\n" + "  {\n" + "      \"quoteAuthor\": \"Margaret Sangster\",\n" + "      \"quoteText\": \"Self-complacency is fatal to progress.\"\n" + "  },\n" + "  {\n" + "      \"quoteAuthor\": \"Buddha\",\n" + "      \"quoteText\": \"Peace comes from within. Do not seek it without.\"\n" + "  },\n" + "  {\n" + "      \"quoteAuthor\": \"Byron Pulsifer\",\n" + "      \"quoteText\": \"What you give is what you get.\"\n" + "  },\n" + "  {\n" + "      \"quoteAuthor\": \"Iris Murdoch\",\n" + "      \"quoteText\": \"We can only learn to love by loving.\"\n" + "  },\n" + "  {\n" + "      \"quoteAuthor\": \"Karen Clark\",\n" + "      \"quoteText\": \"Life is change. Growth is optional. Choose wisely.\"\n" + "  },\n" + "  {\n" + "      \"quoteAuthor\": \"Wayne Dyer\",\n" + "      \"quoteText\": \"You'll see it when you believe it.\"\n" + "  },\n" + "  {\n" + "      \"quoteAuthor\": \"Unknown\",\n" + "      \"quoteText\": \"Today is the tomorrow we worried about yesterday.\"\n" + "  },\n" + "  {\n" + "      \"quoteAuthor\": \"Unknown\",\n" + "      \"quoteText\": \"It's easier to see the mistakes on someone else's paper.\"\n" + "  },\n" + "  {\n" + "      \"quoteAuthor\": \"Unknown\",\n" + "      \"quoteText\": \"Every man dies. Not every man really lives.\"\n" + "  },\n" + "  {\n" + "      \"quoteAuthor\": \"Lao Tzu\",\n" + "      \"quoteText\": \"To lead people walk behind them.\"\n" + "  },\n" + "  {\n" + "      \"quoteAuthor\": \"William Shakespeare\",\n" + "      \"quoteText\": \"Having nothing, nothing can he lose.\"\n" + "  },\n" + "  {\n" + "      \"quoteAuthor\": \"Henry J. Kaiser\",\n" + "      \"quoteText\": \"Trouble is only opportunity in work clothes.\"\n" + "  },\n" + "  {\n" + "      \"quoteAuthor\": \"Publilius Syrus\",\n" + "      \"quoteText\": \"A rolling stone gathers no moss.\"\n" + "  },\n" + "  {\n" + "      \"quoteAuthor\": \"Napoleon Hill\",\n" + "      \"quoteText\": \"Ideas are the beginning points of all fortunes.\"\n" + "  },\n" + "  {\n" + "      \"quoteAuthor\": \"Donald Trump\",\n" + "      \"quoteText\": \"Everything in life is luck.\"\n" + "  },\n" + "  {\n" + "      \"quoteAuthor\": \"Lao Tzu\",\n" + "      \"quoteText\": \"Doing nothing is better than being busy doing nothing.\"\n" + "  },\n" + "  {\n" + "      \"quoteAuthor\": \"Benjamin Spock\",\n" + "      \"quoteText\": \"Trust yourself. You know more than you think you do.\"\n" + "  },\n" + "  {\n" + "      \"quoteAuthor\": \"Confucius\",\n" + "      \"quoteText\": \"Study the past, if you would divine the future.\"\n" + "  },\n" + "  {\n" + "      \"quoteAuthor\": \"Unknown\",\n" + "      \"quoteText\": \"The day is already blessed, find peace within it.\"\n" + "  }]}";



    public static final short USER_UNAVAILABLE = -4000;
    public static final short API_SUCCESS = 2000;
    public static final short API_FAILURE = -2000;
    public static final short TOKEN_ERROR = -500;
    public static final short AUTH_INVALID_G_ACCOUNT = -5000;
    public static final short QUOTES_UPLOADED = 3000;
    public static final short QUOTES_UPLOAD_FAILURE = -3000;
    public static final short PASSWORD_MISMATCH = -5001;

    public static  final short USER_ALREADY_EXISTS=-4000;
    public static  final short USER_CREATED=4000;
}
