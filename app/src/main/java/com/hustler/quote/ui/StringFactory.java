package com.hustler.quote.ui;

import java.util.ArrayList;

/**
 * Created by anvaya5 on 29/01/2018.
 */

public class StringFactory {
    public static String ALONE = "Hasty climbers have sudden falls. \n" +
            "Italian Proverb\n" +

            "We are told never to cross a bridge until we come to it, but this world is owned by men who have 'crossed bridges' in their imagination far ahead of the crowd. \n" +
            "Anon.\n" +

            "When ambition ends, happiness begins. \n" +
            "Proverb\n" +

            "Children, you must remember something. A man without ambition is dead. A man with ambition but no love is dead. A man with ambition and love for his blessings here on earth is ever so alive. \n" +
            "Pearl Bailey\n" +


            "Very few people are ambitious in the sense of having a specific image of what they want to achieve. Most people's sights are only toward the next run, the next increment of money. \n" +
            "Judith M. Bardwick\n" +

            "Ambition -- it is the last infirmity of noble minds. \n" +
            "James Barrie\n" +

            "All ambitions are lawful except those that climb upward on the miseries or credulities of mankind. \n" +
            "Henry Ward Beecher\n" +

            "Ambition. An overmastering desire to be vilified by enemies while living and made ridiculous by friends when dead. \n" +
            "Ambrose Bierce\n" +

            "No bird soars too high if he soars with his own wings. \n" +
            "William Blake\n" +

            "Great ambition is the passion of a great character. Those endowed with it may perform very good or very bad acts. All depends on the principals which direct them. \n" +
            "Napoleon Bonaparte\n" +

            "Shoot for the moon. Even if you miss, you'll land among the stars. \n" +
            "Les Brown\n" +

            "What I aspired to be and was not, comforts me. \n" +
            "Robert Browning\n" +

            "'Tis not what man does which exalts him, but what man Would do! \n" +
            "Robert Browning\n" +

            "Ambition is not what man does... but what man would do. \n" +
            "Robert Browning\n" +

            "There are only two ways of getting on in the world: by one's own industry, or by the stupidity of others. \n" +
            "Jean De La Bruyere\n" +

            "The slave has but one master, the ambitious man has as many as there are persons whose aid may contribute to the advancement of his fortunes. \n" +
            "Jean De La Bruyere\n" +

            "Ambition can creep as well as soar. \n" +
            "Edmund Burke\n" +

            "Like dogs in a wheel, birds in a cage, or squirrels in a chain, ambitious men still climb and climb, with great labor, and incessant anxiety, but never reach the top. \n" +
            "Robert Burton\n" +

            "He who surpasses or subdues mankind, must look down on the hate of those below. \n" +
            "Lord (George Gordon) Byron\n" +

            "As falls the dew on quenchless sands, blood only serves to wash ambition's hands. \n" +
            "Lord (George Gordon) Byron\n" +

            "The men who succeed are the efficient few. They are the few who have the ambition and will power to develop themselves. \n" +
            "Herbert N. Casson\n" +

            "It is commonly supposed that the art of pleasing is a wonderful aid in the pursuit of fortune; but the art of being bored is infinitely more successful. \n" +
            "Sebastian Roch Nicolas Chamfort\n" +

            "Big results require big ambitions. \n" +
            "James Champy\n" +

            "When you are aspiring to the highest place, it is honorable to reach the second or even the third rank. \n" +
            "Marcus Tullius Cicero";
    static String[] vals;
    static ArrayList<String> quotes = new ArrayList<>();
    static ArrayList<String> authors = new ArrayList<>();

    public static void main(String[] args) {
        vals = ALONE.split("\n");
        for (int i = 0; i < vals.length; i++) {
            if (i % 2 == 0) {
                quotes.add(vals[i]);
            } else if (i % 2 == 1) {
                authors.add(vals[i]);
            }
        }
        for (int i = 0; i < quotes.size(); i++) {
            System.out.println("<item>" + quotes.get(i) + "</item>");

        }
        for (int i = 0; i < quotes.size(); i++) {
            System.out.println("<item>" + authors.get(i) + "</item>");


        }
        for (int i = 0; i < quotes.size(); i++) {
            System.out.println("<item>" + "Alone" + "</item>");

        }
        for (int i = 0; i < authors.size(); i++) {

            System.out.println("<item>" + "English" + "</item>");

        }

    }
}
