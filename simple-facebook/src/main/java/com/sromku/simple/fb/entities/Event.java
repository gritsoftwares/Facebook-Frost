package com.sromku.simple.fb.entities;

import com.google.gson.annotations.SerializedName;
import com.sromku.simple.fb.utils.PictureAttributes;

import java.util.Date;

/**
 * @author sromku
 *         // @see https://developers.facebook.com/docs/graph-api/reference/event
 */
public class Event {

    private static final String DESCRIPTION = "description";
    private static final String END_TIME = "end_time";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String OWNER = "owner";
    private static final String PICTURE = "picture";
    private static final String PRIVACY = "privacy";
    private static final String START_TIME = "start_time";
    private static final String TICKET_URI = "ticket_uri";
    private static final String UPDATED_TIME = "updated_time";
    private static final String PLACE = "place";
    private static final String RSVP_STATUS = "rsvp_status";
    private static final String INTERESTED_COUNT = "interested_count";
    private static final String ATTENDING_COUNT = "attending_count";
    private static final String MAYBE_COUNT = "maybe_count";
    private static final String DECLINED_COUNT = "declined_count";
    private static final String CAN_GUESTS_INVITE = "can_guests_invite";
    private static final String GUEST_LIST_ENABLED = "guest_list_enabled";
    private static final String COVER = "cover";

    @SerializedName(DESCRIPTION)
    private String mDescription;

    @SerializedName(END_TIME)
    private Date mEndTime;

    @SerializedName(ID)
    private String mId;

    @SerializedName(NAME)
    private String mName;

    @SerializedName(OWNER)
    private User mOwner;

    /**
     * The URL of the event's picture (only returned if you explicitly include
     * picture in the fields param; example: ?fields=id,name,picture)
     */
    @SerializedName(PICTURE)
    private EventPicture mPicture;

    @SerializedName(PRIVACY)
    private EventPrivacy mPrivacy;

    @SerializedName(START_TIME)
    private Date mStartTime;

    @SerializedName(TICKET_URI)
    private String mTicketUri;

    @SerializedName(UPDATED_TIME)
    private Date mUpdatedTime;

    @SerializedName(PLACE)
    private Place mPlace;

    @SerializedName(RSVP_STATUS)
    private String mRSVP;

    @SerializedName(INTERESTED_COUNT)
    private Integer mInterestedCount;

    @SerializedName(ATTENDING_COUNT)
    private Integer mAttendingCount;

    @SerializedName(MAYBE_COUNT)
    private Integer mMaybeCount;

    @SerializedName(DECLINED_COUNT)
    private Integer mDeclinedCount;

    @SerializedName(CAN_GUESTS_INVITE)
    private boolean mCanGuestsInvite;

    @SerializedName(GUEST_LIST_ENABLED)
    private boolean mIsGuestListEnabled;

    @SerializedName(COVER)
    private EventCover mCover;

    /**
     * The attendance options of the user. He can accept and <b>attend</b> the
     * event, or say <b>maybe</b>, or totally <b>decline</b> the invitation.
     * <p/>
     * <li>{@link #ATTENDING}</li> <li>{@link #MAYBE}</li> <li>{@link #DECLINED}
     * </li><br>
     *
     * @author sromku
     */
    public enum EventDecision {
        ALL("all"), //not actually a node; will be modified from GetEventsAction
        /**
         * Events that user decided to attend
         */
        ATTENDING("attending"),
        /**
         * Events that user said maybe
         */
        MAYBE("maybe"),
        /**
         * Events that user created
         */
        CREATED("created"),
        /**
         * Events that user not replied
         */
        NOT_REPLIED("not_replied"),
        /**
         * Events that user declined
         */
        DECLINED("declined");

        private String graphNode;

        private EventDecision(String graphNode) {
            this.graphNode = graphNode;
        }

        public String getGraphNode() {
            return graphNode;
        }
    }

    public enum EventPrivacy {
        OPEN("open"),
        SECRET("secret"),
        FRIENDS("friends"),
        CLOSED("closed");

        private String value;

        private EventPrivacy(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static EventPrivacy fromValue(String value) {
            for (EventPrivacy privacyEnum : values()) {
                if (privacyEnum.value.equals(value)) {
                    return privacyEnum;
                }
            }
            return null;
        }
    }

    public class EventPicture {
        EventPictureData data;
    }

    public class EventPictureData {
        String url;
        Integer height, width;
        boolean is_silhouette;
    }

    /**
     * The long-form description of the event.
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * The end time of the event, if one has been set.
     */
    public Date getEndTime() {
        return mEndTime;
    }

    /**
     * The event Id.
     */
    public String getId() {
        return mId;
    }

    /**
     * The event title.
     */
    public String getName() {
        return mName;
    }

    /**
     * The profile that created the event.
     */
    public User getOwner() {
        return mOwner;
    }

    /**
     * The URL of the event's picture.
     */
    public String getPictureURL() {
        return mPicture.data.url;
    }

    public Integer getPictureHeight() {
        return mPicture.data.height;
    }

    public Integer getPictureWidth() {
        return mPicture.data.width;
    }

    public boolean getPictureIsSilhouette() {
        return mPicture.data.is_silhouette;
    }

    public class EventCover {
        String source;
        String id;
    }

    public String getCoverURL() {
        if (mCover == null) return null;
        return mCover.source;
    }

    public String getCoverID() {
        if (mCover == null) return null;
        return mCover.id;
    }

    /**
     * The visibility of this event.
     */
    public EventPrivacy getPrivacy() {
        return mPrivacy;
    }

    /**
     * The start time of the event, as you want it to be displayed on facebook.
     */
    public Date getStartTime() {
        return mStartTime;
    }

    /**
     * The URL to a location to buy tickets for this event (on Events for Pages
     * only).
     */
    public String getTicketUri() {
        return mTicketUri;
    }

    /**
     * The last time the event was updated.
     */
    public Date getUpdatedTime() {
        return mUpdatedTime;
    }

    /**
     * The location of this event.
     */
    public Place getPlace() {
        return mPlace;
    }

    public String getRsvpStatus() {
        return mRSVP;
    }

    public Integer getInterestedCount() {
        return mInterestedCount;
    }

    public Integer getAttendingCount() {
        return mAttendingCount;
    }

    public Integer getMaybeCount() {
        return mMaybeCount;
    }

    public Integer getDeclinedCount() {
        return mDeclinedCount;
    }

    public boolean canGuestsInvite() {
        return mCanGuestsInvite;
    }

    public boolean isGuestListEnabled() {
        return mIsGuestListEnabled;
    }

}
