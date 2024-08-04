package com.project.house.rental.constant;

public class FilterConstant {

    public static final String IS_DELETED = "isDeleted";
    public static final String CONDITION = "is_deleted = :isDeleted";

    public static final String IS_BLOCKED = "isBlocked";
    public static final String BLOCKED_CONDITION = "is_blocked = :isBlocked";

    public static final String STATUS = "status";
    public static final String STATUS_CONDITION = "status = :status";

    public static final String DELETE_CITY_FILTER = "deletedCityFilter";
    public static final String DELETE_DISTRICT_FILTER = "deletedDistrictFilter";
    public static final String DELETE_AMENITY_FILTER = "deletedAmenityFilter";
    public static final String DELETE_ROOM_TYPE_FILTER = "deletedRoomTypeFilter";
    public static final String DELETE_REVIEW_FILTER = "deletedReviewFilter";
    public static final String DELETE_REPORT_FILTER = "deletedReportFilter";
    public static final String DELETE_PROPERTY_IMAGE_FILTER = "deletedPropertyImageFilter";
    public static final String DELETE_PROPERTY_FILTER = "deletedPropertyFilter";
    public static final String BLOCK_PROPERTY_FILTER = "blockedPropertyFilter";
    public static final String STATUS_PROPERTY_FILTER = "statusPropertyFilter";
    public static final String DELETE_FAVORITE_FILTER = "deletedFavoriteFilter";
    public static final String DELETE_ROLE_FILTER = "deletedRoleFilter";
}
