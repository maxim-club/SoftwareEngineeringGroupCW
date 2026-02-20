package com.studyspaces.UserTests;

import com.studyspaces.spacefinder.model.StudySession;
import com.studyspaces.spacefinder.model.UserRecord;
import com.studyspaces.spacefinder.repository.UserRepository;
import com.studyspaces.spacefinder.service.UserManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserManagerTests {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserManager userManager;

    private UserRecord user;


    @BeforeEach
    void setup() {
        user = new UserRecord();
        user.setUsername("max");
        user.setPassword("secret");
        user.setStudySessionList(new ArrayList<>());
    }

    //Login tests

    @Test
    void checkLogin_correctPassword_returnsTrue() {
        when(userRepository.findUserRecordByUsername("max"))
                .thenReturn(Optional.of(user));

        Boolean result = userManager.checkLogin("max", "secret");

        assertTrue(result);
    }

    @Test
    void checkLogin_wrongPassword_returnsFalse() {
        when(userRepository.findUserRecordByUsername("max"))
                .thenReturn(Optional.of(user));

        Boolean result = userManager.checkLogin("max", "wrong");

        assertFalse(result);
    }

    @Test
    void checkLogin_userNotFound_throwsException() {
        when(userRepository.findUserRecordByUsername("max"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> userManager.checkLogin("max", "secret"));
    }

    //Study Session Tests

    @Test
    void startStudySession_addsNewSession() {
        when(userRepository.findUserRecordByUsername("max"))
                .thenReturn(Optional.of(user));

        userManager.startStudySession("max", "ROOM1");

        assertEquals(1, user.getStudySessionList().size());

        StudySession session = user.getStudySessionList().get(0);

        assertEquals("ROOM1", session.getRoomId());
        assertNotNull(session.getStartTimestamp());
        assertNull(session.getEndTimestamp());

        verify(userRepository).save(user);
    }

    @Test
    void startStudySession_userNotFound_throwsException() {
        when(userRepository.findUserRecordByUsername("max"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> userManager.startStudySession("max", "ROOM1"));
    }

    @Test
    void endStudySession_setsEndTimestamp_andDuplicatesSession() {
        StudySession existing = new StudySession();
        existing.setRoomId("ROOM1");
        existing.setStartTimestamp(100L);
        existing.setEndTimestamp(null);

        user.getStudySessionList().add(existing);

        when(userRepository.findUserRecordByUsername("max"))
                .thenReturn(Optional.of(user));

        userManager.endStudySession("max");

        assertEquals(1, user.getStudySessionList().size());

        StudySession updated =
                user.getStudySessionList().get(0);

        assertNotNull(updated.getEndTimestamp());

        verify(userRepository).save(user);
    }

    @Test
    void endStudySession_userNotFound_throwsException() {
        when(userRepository.findUserRecordByUsername("max"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> userManager.endStudySession("max"));
    }

    @Test
    void endStudySession_noSessions_throwsIndexException() {
        when(userRepository.findUserRecordByUsername("max"))
                .thenReturn(Optional.of(user));

        assertThrows(IndexOutOfBoundsException.class,
                () -> userManager.endStudySession("max"));
    }


}
