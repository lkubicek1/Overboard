package com.teamtreehouse.techdegree.overboard.model;

import com.teamtreehouse.techdegree.overboard.exc.AnswerAcceptanceException;
import com.teamtreehouse.techdegree.overboard.exc.VotingException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class UserTest {

    private Board board;
    private User questioner;
    private User rogueQuestioner;
    private User answerer;
    private Question question;
    private Answer answer;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        board = new Board("Test Board");
        questioner = new User(board, "The original questioner");
        rogueQuestioner = new User(board, "The rogue questioner");
        answerer = new User(board, "The answerer");
        question = questioner.askQuestion("Test Question");
        answer = answerer.answerQuestion(question, "Test answer");
    }

    @Test
    public void upVoteQuestionIncrementsReputation() throws Exception {
        answerer.upVote(question);

        assertEquals("Expect reputation to be 5", 5, questioner.getReputation());
    }

    @Test
    public void upVoteAnswerIncrementsReputation() throws Exception {
        questioner.upVote(answer);

        assertEquals("Expect reputation to be 10", 10, answerer.getReputation());
    }

    @Test
    public void downVoteAnswerDecrementsReputation() throws Exception {
        questioner.upVote(answer);
        rogueQuestioner.downVote(answer);

        assertEquals("Expect reputation to be 9", 9, answerer.getReputation());
    }

    @Test
    public void acceptedAnswerIncrementsReputation() throws Exception {
        questioner.acceptAnswer(answer);

        assertEquals("Expect reputation to be 15", 15, answerer.getReputation());
    }

    @Test
    public void upVoteQuestionByOwnerNotAllowed() throws Exception {
        thrown.expect(VotingException.class);
        questioner.upVote(question);
    }

    @Test
    public void downVoteQuestionByOwnerNotAllowed() throws Exception {
        thrown.expect(VotingException.class);
        questioner.downVote(question);
    }

    @Test
    public void upVoteAnswerByOwnerNotAllowed() throws Exception {
        thrown.expect(VotingException.class);
        answerer.upVote(answer);
    }

    @Test
    public void downVoteAnswerByOwnerNotAllowed() throws Exception {
        thrown.expect(VotingException.class);
        answerer.downVote(answer);
    }

    @Test
    public void acceptAnswerOnlyAllowedByOriginalQuestioner() throws Exception {
        thrown.expect(AnswerAcceptanceException.class);
        thrown.expectMessage("Only The original questioner can accept this answer as it is their question");
        rogueQuestioner.acceptAnswer(answer);
    }
}