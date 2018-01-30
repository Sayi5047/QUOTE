package com.hustler.quote.ui;

import java.util.ArrayList;

/**
 * Created by anvaya5 on 29/01/2018.
 */

public class StringFactory {
    public static String ALONE = "A poor beauty finds more lovers than husbands. \n" +
            "English Proverb\n" +
            "Beauty, unaccompanied by virtue, is as a flower without perfume. \n" +
            "Proverb\n" +
            "The loveliest faces are to be seen by moonlight, when one sees half with the eye and half with the fancy. \n" +
            "Proverb\n" +
            "Beauty is a good letter of introduction. \n" +
            "Proverb\n" +
            "There is nothing that makes its way more directly to the soul than beauty. \n" +
            "Joseph Addison\n" +
            "Let there be nothing within thee that is not very beautiful and very gentle, and there will be nothing without thee that is not beautiful and softened by the spell of thy presence. \n" +
            "James Allen\n" +
            "....Then he felt quite ashamed, and hid his head under his wing; for he did not know what to do, he was so happy, and yet not at all proud. He had been persecuted and despised for his ugliness, and now he heard them say he was the most beautiful of all the birds. Even the elder-tree bent down its bows into the water before him, and the sun shone warm and bright. He would never became vain or conceited, and would always remembered how it felt to be despised and teased, and he was very sorry for all the creatures who are so treated merely because they are different from those around them. Then he rustled his feathers, curved his slender neck, and cried joyfully, from the depths of his heart, \n" +
            "Hans Christian Andersen\n" +
            "What ever beauty may be, it has for its basis order, and for its essence unity. \n" +
            "Father Andre\n" +
            "Beauty is one of the rare things that do not lead to doubt of God. \n" +
            "Jean Anouilh\n" +
            "Things are beautiful if you love them. \n" +
            "Jean Anouilh\n" +
            "Beauty is one of the rare things that do not lead to doubt of God. \n" +
            "Jean Anouilh\n" +
            "Personal beauty is a greater recommendation than any letter of reference. \n" +
            "Aristotle\n" +
            "Beauty depends on size as well as symmetry. No very small animal can be beautiful, for looking at it takes so small a portion of time that the impression of it will be confused. Nor can any very large one, for a whole view of it cannot be had at once, and so there will be no unity and completeness. \n" +
            "Aristotle\n" +
            "Strange that the vanity which accompanies beauty --excusable, perhaps, when there is such great beauty, or at any rate understandable --should persist after the beauty was gone. \n" +
            "Mary Arnim\n" +
            "The best part of beauty is that which no picture can express. \n" +
            "Francis Bacon\n" +
            "There is no excellent beauty that hath not some strangeness in the proportion. \n" +
            "Francis Bacon\n" +
            "Beauty is but the sensible image of the Infinite. Like truth and justice it lives within us; like virtue and the moral law it is a companion of the soul. \n" +
            "George Bancroft\n" +
            "Beauty itself is but the sensible image of the infinite. \n" +
            "George Bancroft\n" +
            "Im not ugly, but my beauty is a total creation. \n" +
            "Tyra Banks\n" +
            "Beauty is desired in order that it may be befouled; not for its own sake, but for the joy brought by the certainty of profaning it. \n" +
            "Georges Bataille\n" +
            "All forms of beauty, like all possible phenomena, contain an element of the eternal and an element of the transitory -- of the absolute and of the particular. Absolute and eternal beauty does not exist, or rather it is only an abstraction creamed from the general surface of different beauties. The particular element in each manifestation comes from the emotions: and just as we have our own particular emotions, so we have our own beauty. \n" +
            "Charles Baudelaire\n" +
            "There are as many kinds of beauty as there are habitual ways of seeking happiness. \n" +
            "Charles Baudelaire\n" +
            "The loveliest face in all the world will not please you if you see it suddenly eye to eye, at a distance of half an inch from your own. \n" +
            "Max Beerbohm\n" +
            "The idea that happiness could have a share in beauty would be too much of a good thing. \n" +
            "Walter Benjamin\n" +
            "Beauty is in the heart of the beholder. \n" +
            "Al Bernstein";
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
            System.out.println("<item>" + "Beauty" + "</item>");

        }
        for (int i = 0; i < authors.size(); i++) {

            System.out.println("<item>" + "English" + "</item>");

        }

    }
}
