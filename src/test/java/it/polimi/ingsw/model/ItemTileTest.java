package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.ItemType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ItemTile tests")
public class ItemTileTest {

    @Nested
    @DisplayName("getType method")
    class GetTypeTests {
        @Test
        @DisplayName("Returns correct ItemType")
        public void typeGet_ReturnsCorrectItemType() {
            ItemTile itemTile = new ItemTile(ItemType.TROPHY);
            assertEquals(ItemType.TROPHY, itemTile.getType());
        }
    }

    @Nested
    @DisplayName("setType method")
    class SetTypeTests {
        @Test
        @DisplayName("Changes ItemType correctly")
        public void typeSet_ChangesItemTypeCorrectly() {
            ItemTile itemTile = new ItemTile(ItemType.CAT);
            itemTile.setType(ItemType.FRAME);
            assertEquals(ItemType.FRAME, itemTile.getType());
        }
    }

    @Nested
    @DisplayName("setGroupId method")
    class SetGroupIdTests {
        @Test
        @DisplayName("Sets GroupId correctly")
        public void groupSet_SetsGroupIdCorrectly() {
            ItemTile itemTile = new ItemTile(ItemType.PLANT);
            itemTile.setGroupId(5);
            assertEquals(5, itemTile.getGroupId());
        }

    }

    @Nested
    @DisplayName("isEmpty method")
    class IsEmptyTests {
        @Test
        @DisplayName("Returns true if ItemType is EMPTY")
        public void isTypeEmpty_ReturnsTrueIfItemTypeIsEmpty() {
            ItemTile itemTile = new ItemTile(ItemType.EMPTY);
            assertTrue(itemTile.isEmpty());
        }

        @Test
        @DisplayName("Returns false if ItemType is not EMPTY")
        public void isTypeEmpty_ReturnsFalseIfItemTypeIsNotEmpty() {
            ItemTile itemTile = new ItemTile(ItemType.PLANT);
            assertFalse(itemTile.isEmpty());
        }
    }

    @Nested
    @DisplayName("isForbidden method")
    class IsForbiddenTests {
        @Test
        @DisplayName("Returns true if ItemType is FORBIDDEN")
        public void isTypeForbidden_ReturnsTrueIfItemTypeIsForbidden() {
            ItemTile itemTile = new ItemTile(ItemType.FORBIDDEN);
            assertTrue(itemTile.isForbidden());
        }

        @Test
        @DisplayName("Returns false if ItemType is not FORBIDDEN")
        public void isTypeForbidden_ReturnsFalseIfItemTypeIsNotForbidden() {
            ItemTile itemTile = new ItemTile(ItemType.PLANT);
            assertFalse(itemTile.isForbidden());
        }
    }

    @Nested
    @DisplayName("hasSomething method")
    class HasSomethingTests {
        @Test
        @DisplayName("Returns true if ItemType is not FORBIDDEN or EMPTY")
        public void hasTypeSomething_ReturnsTrueIfItemTypeIsNotForbiddenOrEmpty() {
            ItemTile itemTile1 = new ItemTile(ItemType.TROPHY);
            assertTrue(itemTile1.hasSomething());

            ItemTile itemTile2 = new ItemTile(ItemType.PLANT);
            assertTrue(itemTile2.hasSomething());
        }

        @Test
        @DisplayName("Returns false if ItemType is FORBIDDEN or EMPTY")
        public void hasTypeSomething_ReturnsFalseIfItemTypeIsForbiddenOrEmpty() {
            ItemTile itemTile1 = new ItemTile(ItemType.EMPTY);
            assertFalse(itemTile1.hasSomething());

            ItemTile itemTile2 = new ItemTile(ItemType.FORBIDDEN);
            assertFalse(itemTile2.hasSomething());
        }
    }

    @Nested
    @DisplayName("toString method")
    class ToStringTests {
        @Test
        @DisplayName("Returns the ItemType as a string")
        public void toString_ReturnsItemTypeAsString() {
            ItemTile itemTile = new ItemTile(ItemType.TROPHY);
            assertEquals("T", itemTile.toString());
        }

        @Test
        @DisplayName("set and get Image Id")
        public void setAndgetImageId_Test() {
            ItemTile empty=new ItemTile();
            ItemTile itemTile = new ItemTile(ItemType.TROPHY);
            itemTile.setImageId(123);
            assertEquals(123, itemTile.getImageId());
        }
    }
}
