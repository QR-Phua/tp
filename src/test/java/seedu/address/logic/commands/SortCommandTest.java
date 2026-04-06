package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonSortField;
import seedu.address.model.person.PersonSortOrder;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for
 * {@code SortCommand}.
 */
public class SortCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_sortNameAscending_success() {
        SortCommand sortCommand = new SortCommand(PersonSortField.NAME, PersonSortOrder.ASCENDING);
        String expectedMessage = String.format(SortCommand.MESSAGE_SUCCESS, "name", "ascending");
        expectedModel.updateDisplayedPersonListSort(PersonSortField.NAME, PersonSortOrder.ASCENDING);

        assertCommandSuccess(sortCommand, model, expectedMessage, expectedModel);
        assertEquals(ALICE, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_sortRateDescending_success() {
        SortCommand sortCommand = new SortCommand(PersonSortField.RATE, PersonSortOrder.DESCENDING);
        String expectedMessage = String.format(SortCommand.MESSAGE_SUCCESS, "rate", "descending");
        expectedModel.updateDisplayedPersonListSort(PersonSortField.RATE, PersonSortOrder.DESCENDING);

        assertCommandSuccess(sortCommand, model, expectedMessage, expectedModel);
        assertEquals(BENSON, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_sortRateAscending_tieBreakByName() {
        Person aaron = new PersonBuilder().withName("Aaron Aaa").withPhone("61111111")
                .withEmail("aaron@tie.test").withAddress("1 Tie St").withSubject("Math").withRate("30").build();
        Person amyTie = new PersonBuilder().withName("Amy Alpha").withPhone("62222222")
                .withEmail("amy@tie.test").withAddress("2 Tie St").withSubject("Math").withRate("50").build();
        Person zoeTie = new PersonBuilder().withName("Zoe Zee").withPhone("63333333")
                .withEmail("zoe@tie.test").withAddress("3 Tie St").withSubject("Math").withRate("50").build();

        AddressBook ab = new AddressBookBuilder().withPerson(zoeTie).withPerson(aaron).withPerson(amyTie).build();
        model = new ModelManager(ab, new UserPrefs());
        expectedModel = new ModelManager(ab, new UserPrefs());

        SortCommand sortCommand = new SortCommand(PersonSortField.RATE, PersonSortOrder.ASCENDING);
        expectedModel.updateDisplayedPersonListSort(PersonSortField.RATE, PersonSortOrder.ASCENDING);
        assertCommandSuccess(sortCommand, model,
                String.format(SortCommand.MESSAGE_SUCCESS, "rate", "ascending"), expectedModel);

        assertEquals(aaron, model.getFilteredPersonList().get(0));
        assertEquals(amyTie, model.getFilteredPersonList().get(1));
        assertEquals(zoeTie, model.getFilteredPersonList().get(2));

        // Same tie-break (name ascending) when primary order is rate descending
        model = new ModelManager(ab, new UserPrefs());
        expectedModel = new ModelManager(ab, new UserPrefs());
        SortCommand sortDesc = new SortCommand(PersonSortField.RATE, PersonSortOrder.DESCENDING);
        expectedModel.updateDisplayedPersonListSort(PersonSortField.RATE, PersonSortOrder.DESCENDING);
        assertCommandSuccess(sortDesc, model,
                String.format(SortCommand.MESSAGE_SUCCESS, "rate", "descending"), expectedModel);

        assertEquals(amyTie, model.getFilteredPersonList().get(0));
        assertEquals(zoeTie, model.getFilteredPersonList().get(1));
        assertEquals(aaron, model.getFilteredPersonList().get(2));
    }

    @Test
    public void execute_sortThenDeleteByDisplayedIndex_success() {
        SortCommand sortCommand = new SortCommand(PersonSortField.NAME, PersonSortOrder.ASCENDING);
        sortCommand.execute(model);

        Person toDelete = model.getFilteredPersonList().get(INDEX_THIRD_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_THIRD_PERSON);

        Model expectedAfterDelete = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedAfterDelete.updateDisplayedPersonListSort(PersonSortField.NAME, PersonSortOrder.ASCENDING);
        expectedAfterDelete.deletePerson(toDelete);

        assertCommandSuccess(deleteCommand, model,
                DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                expectedAfterDelete);
    }

    @Test
    public void execute_sortThenEdit_repositionsInDisplayedList() {
        SortCommand sortCommand = new SortCommand(PersonSortField.NAME, PersonSortOrder.ASCENDING);
        sortCommand.execute(model);

        Person firstSorted = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstSorted).withName("Zzzz Zebra").build();
        EditCommand.EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName("Zzzz Zebra").build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        Model expectedAfterEdit = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedAfterEdit.updateDisplayedPersonListSort(PersonSortField.NAME, PersonSortOrder.ASCENDING);
        expectedAfterEdit.setPerson(firstSorted, editedPerson);

        assertCommandSuccess(editCommand, model,
                EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                expectedAfterEdit);
        assertEquals(editedPerson, model.getFilteredPersonList().get(model.getFilteredPersonList().size() - 1));
    }

    @Test
    public void equals() {
        SortCommand sortNameAsc = new SortCommand(PersonSortField.NAME, PersonSortOrder.ASCENDING);
        SortCommand sortNameAscCopy = new SortCommand(PersonSortField.NAME, PersonSortOrder.ASCENDING);
        SortCommand sortNameDesc = new SortCommand(PersonSortField.NAME, PersonSortOrder.DESCENDING);
        SortCommand sortRateAsc = new SortCommand(PersonSortField.RATE, PersonSortOrder.ASCENDING);

        assertTrue(sortNameAsc.equals(sortNameAsc));
        assertTrue(sortNameAsc.equals(sortNameAscCopy));
        assertFalse(sortNameAsc.equals(null));
        assertFalse(sortNameAsc.equals(1));
        assertFalse(sortNameAsc.equals(sortNameDesc));
        assertFalse(sortNameAsc.equals(sortRateAsc));
    }
}
