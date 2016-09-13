package com.matmr.restaurantpoll.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class RestaurantTest {
	
	private String NAME = "name";
    private String DESCRIPTION = "description";
    private Category CATEGORY = Category.BARBECUE;

    @Test
    public void build_MandatoryInformationGiven_ShouldCreateNewObjectAndSetMandatoryInformation() {
        Restaurant built = Restaurant.getBuilder(NAME).build();

        assertNull(built.getId());
        assertNull(built.getDescription());
        assertEquals(NAME, built.getName());
    }

    @Test
    public void build_AllInformationGiven_ShouldCreateNewObjectAndSetAllInformation() {
    	Restaurant built = Restaurant.getBuilder(NAME)
                .description(DESCRIPTION)
                .category(CATEGORY)
                .build();

        assertNull(built.getId());
        assertEquals(NAME, built.getName());
        assertEquals(DESCRIPTION, built.getDescription());
        assertEquals(CATEGORY, built.getCategory());
    }
    
    @Test
    public void prePersist_ShouldSetCreationTimeAndModificationTime() {
    	Restaurant restaurant = new Restaurant();
    	restaurant.prePersist();

        assertNull(restaurant.getId());
        assertNotNull(restaurant.getCreationTime());
        assertNull(restaurant.getDescription());
        assertNotNull(restaurant.getModificationTime());
        assertNull(restaurant.getName());
        assertNull(restaurant.getCategory());
        assertEquals(restaurant.getCreationTime(), restaurant.getModificationTime());
    }

    @Test
    public void preUpdate_ShouldUpdateOnlyModificationTime() {
    	Restaurant restaurant = new Restaurant();
    	restaurant.prePersist();

        pause(1000);

        restaurant.preUpdate();

        assertNull(restaurant.getId());
        assertNotNull(restaurant.getCreationTime());
        assertNull(restaurant.getDescription());
        assertNotNull(restaurant.getModificationTime());
        assertNull(restaurant.getName());
        assertNull(restaurant.getCategory());
        assertTrue(restaurant.getModificationTime().isAfter(restaurant.getCreationTime()));
    }

    private void pause(long timeInMillis) {
        try {
            Thread.currentThread();
			Thread.sleep(timeInMillis);
        }
        catch (InterruptedException e) {
            //Do Nothing
        }
    }
    
}
