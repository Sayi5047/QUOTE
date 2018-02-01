package com.hustler.quote.ui;

import java.util.ArrayList;

/**
 * Created by anvaya5 on 29/01/2018.
 */

public class StringFactory {
    public static String ALONE = "God will provide -- ah, if only He would till He does! \n" +
            "Yiddish Proverb\n" +
            
            "If God lived on earth, people would break his windows. \n" +
            "Yiddish Proverb\n" +
            
            "God is closest to those with broken hearts. \n" +
            "Yiddish Proverb\n" +
            
            "Forsake not God till you find a better master. \n" +
            "Scottish Proverb\n" +
            
            "Every man for himself and God for us all. \n" +
            "Proverb\n" +
            
            "What men usually ask for when they pray to God is, that two and two may not make four. \n" +
            "Proverb\n" +
            
            "Man does what he can, God does what he will. \n" +
            "Proverb\n" +
            
            "I find it interesting that the meanest life, the poorest existence, is attributed to God's will, but as human beings become more affluent, as their living standard and style begin to ascend the material scale, God descends the scale of responsibility at a commensurate speed. \n" +
            "Maya Angelou\n" +
            
            "With God, what is terrible is that one never knows whether it's not just a trick of the devil. \n" +
            "Jean Anouilh\n" +
            
            "God is Love, we are taught as children to believe. But when we first begin to get some inkling of how He loves us, we are repelled; it seems so cold, indeed, not love at all as we understand the word. \n" +
            "W. H. Auden\n" +
            
            "God brings men into deep waters not to drown them, but to cleanse them. \n" +
            "John H. Aughey\n" +
            
            "They that deny a God destroy man's nobility; for certainly man is of kin to the beasts by his body; and, if he be not of kin to God by his spirit, he is a base and ignoble creature. \n" +
            "Francis Bacon\n" +
            
            "God hangs the greatest weights upon the smallest wires. \n" +
            "Francis Bacon\n" +
            
            "I went to the root of things, and found nothing but Him alone. \n" +
            "Mira Bai\n" +
            
            "We know all their gods; they ignore ours. What they call our sins are our gods, and what they call their gods, we name otherwise. \n" +
            "Natalie Clifford Barney\n" +
            
            "To place oneself in the position of God is painful: being God is equivalent to being tortured. For being God means that one is in harmony with all that is, including the worst. The existence of the worst evils is unimaginable unless God willed them. \n" +
            "Georges Bataille\n" +
            
            "How can one better magnify the Almighty than by sniggering with him at his little jokes, particularly the poorer ones. \n" +
            "Samuel Beckett\n" +
            
            "The bastard! He doesn't exist! \n" +
            "Samuel Beckett\n" +
            
            "God's providence is on the side of clear heads. \n" +
            "Henry Ward Beecher\n" +
            
            "And now Israel, what does the Lord your God require from you, but to fear the Lord your God, to walk in all His ways and love Him, and to serve the Lord your God with all your heart and all your soul. Deuteronomy 10:12 \n" +
            "Bible\n" +
            
            "But from there you will seek the Lord you God and you will find Him if you search for Him with all your heart and all your soul. Deuteronomy 4:29 \n" +
            "Bible\n" +
            
            "For now we see through a glass darkly, but then face to face; now I know in part, but then shall I know even as also I am known. I Corinthians \n" +
            "Bible\n" +
            
            "God is not a man, that he should lie, nor a son of man, that he should repent; has he not said, and will he not do it? Or has he spoken, and will he not make it good? Numbers 23:19 \n" +
            "Bible\n" +
            
            "Oh the depth of both the wisdom and riches of God! How unsearchable are his judgments, and his ways beyond understanding. \n" +
            "Bible\n" +
            
            "A god who let us prove his existence would be an idol. \n" +
            "Dietrich Bonhoeffer";
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
            System.out.println("<item>" + "GOD" + "</item>");

        }
        for (int i = 0; i < authors.size(); i++) {

            System.out.println("<item>" + "English" + "</item>");

        }


    }
}
