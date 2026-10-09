/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class ExtractTest {

    /*
     * Testing strategy
     *
     * getTimespan:
     *   number of tweets: 1, > 1
     *   order of tweets: sorted by time, not sorted
     *   timestamps: all different, some equal
     *
     * getMentionedUsers:
     *   number of mentions: 0, 1, > 1
     *   position of mention: start, middle, end of text
     *   same user mentioned in different case
     *   "@" preceded by a username character (email address), not a mention
     *   mention followed by punctuation
     *
     * Usernames are case-insensitive, so results are compared in lowercase.
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:30:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "ben", "@alyssa see you at the talk, @Bbitdiddle", d3);
    private static final Tweet tweet4 = new Tweet(4, "eva", "mail me at eva@mit.edu, thanks @ALYSSA!", d1);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }
    
    @Test
    public void testGetTimespanOneTweet() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet2));
        
        assertEquals("expected start", d2, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }
    
    @Test
    public void testGetTimespanUnsortedSameTime() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet3, tweet1, tweet4, tweet2));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d3, timespan.getEnd());
    }
    
    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }
    
    @Test
    public void testGetMentionedUsersStartAndEnd() {
        Set<String> mentionedUsers = lowercase(Extract.getMentionedUsers(Arrays.asList(tweet3)));
        
        assertEquals(new HashSet<>(Arrays.asList("alyssa", "bbitdiddle")), mentionedUsers);
    }
    
    @Test
    public void testGetMentionedUsersEmailNotMention() {
        Set<String> mentionedUsers = lowercase(Extract.getMentionedUsers(Arrays.asList(tweet4)));
        
        assertEquals(new HashSet<>(Arrays.asList("alyssa")), mentionedUsers);
    }
    
    @Test
    public void testGetMentionedUsersDifferentCase() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1, tweet3, tweet4));
        
        assertEquals("expected each user once", 2, mentionedUsers.size());
        assertEquals(new HashSet<>(Arrays.asList("alyssa", "bbitdiddle")), lowercase(mentionedUsers));
    }
    
    private static Set<String> lowercase(Set<String> users) {
        Set<String> result = new HashSet<>();
        for (String user : users) {
            result.add(user.toLowerCase());
        }
        return result;
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * Extract class that follows the spec. It will be run against several staff
     * implementations of Extract, which will be done by overwriting
     * (temporarily) your version of Extract with the staff's version.
     * DO NOT strengthen the spec of Extract or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Extract, because that means you're testing a
     * stronger spec than Extract says. If you need such helper methods, define
     * them in a different class. If you only need them in this test class, then
     * keep them in this test class.
     */

}
