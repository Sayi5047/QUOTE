
package com.hustler.quote.ui;

import java.util.ArrayList;

/**
 * Created by anvaya5 on 29/01/2018.
 */
public class StringFactory {
    public static String Cates =
            "Change\n" +
                    "Character\n" +
                    "Communication\n" +
                    "Computers\n" +
                    "Creativity\n" +
                    "Dignity\n" +
                    "Duty\n" +
                    "Death\n" +
                    "Design\n" +
                    "Diet\n" +
                    "Dreams\n" +
                    "Effort\n" + "Education\n" +
                    "Environmental\n" +
                    "Equality\n" +
                    "Experience\n" +
                    "Failure\n" +
                    "Faith\n" +
                    "Family\n" +
                    "Father\n" +
                    "Fear\n" +
                    "Food\n" +
                    "Forgiveness\n" +
                    "Freedom\n" +
                    "Friendship\n" +
                    "Future\n" +
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
                    "Life \n" +
                    "Love \n" +
                    "Marriage\n" +
                    "Medical\n" +
                    "Memory\n" +
                    "Men\n" + "Mom\n" +
                    "Money\n" +
                    "Mercy\n" +
                    "Motivational \n" +
                    "Morals\n" +
                    "Music\n" +
                    "Nature\n" +
                    "New Year\n" +
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
                    "Sex\n" +
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
                    "Women\n" +
                    "Work\n" +
                    "Youth\n";
    public static String ALONE2 = "";
    static String[] vals;
    static ArrayList<String> quotes = new ArrayList<>();
    static ArrayList<String> authors = new ArrayList<>();
    static int[] times;
    static String[] total_strings;

    public static void main(String[] args) {
        vals = Cates.split("\n");
        for (int i = 0; i < vals.length; i++) {
            System.out.println(vals[i]);
        }
//        times = new int[vals.length];
        times = new int[]{
                24,
                25,
                25,
                25,
                25,
                25,
                25,
                23,
                25,
                25,
                24,
                25,
                25,
                25,
                24,
                25,
                25,
                25,
                25,
                25,
                25,
                25,
                25,
                25,
                25,
                25,
                25,
                25,
                25,
                24,
                25,
                25,
                25,
                25,
                25,
                25,
                25,
                25,
                32,
                25,
                25,
                25,
                25,
                25,
                25,
                25,
                20,
                75,
                25,
                25,
                25,
                24,
                24,
                24,
                14,
                96,
                25,
                25,
                25,
                4,
                25,
                24,
                25,
                25,
                25,
                25,
                25,
                25,
                25,
                25,
                25,
                25,
                25,
                25,
                23,
                25,
                25,
                25,
                25,
                50,
                25,
                25,
                8,
                25,
                25,
                25,
                25,
                25,
                25,
                25,
                25};

        System.out.println(vals.length);
        System.out.println(times.length);
        int value = 0;
        for (int i = 0; i < vals.length; i++) {
            for (int j = 0; j < times[i]; j++) {
                System.out.println("<item>" + vals[i] + "</item>");
                value++;
            }
        }
        System.out.println(value);
        for (int i = 0; i < value; i++) {

                System.out.println("<item>" + "English" + "</item>");


        }
    }
}
