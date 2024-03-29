/*
 * This file is generated by jOOQ.
 */

package edu.java.domain.jooq;

import edu.java.domain.jooq.tables.Chat;
import edu.java.domain.jooq.tables.Link;
import edu.java.domain.jooq.tables.Subscription;
import edu.java.domain.jooq.tables.records.ChatRecord;
import edu.java.domain.jooq.tables.records.LinkRecord;
import edu.java.domain.jooq.tables.records.SubscriptionRecord;
import javax.annotation.processing.Generated;
import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in
 * public.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<ChatRecord> CHAT_PKEY = Internal.createUniqueKey(Chat.CHAT, DSL.name("chat_pkey"), new TableField[] { Chat.CHAT.ID }, true);
    public static final UniqueKey<LinkRecord> LINK_PKEY = Internal.createUniqueKey(Link.LINK, DSL.name("link_pkey"), new TableField[] { Link.LINK.ID }, true);
    public static final UniqueKey<LinkRecord> LINK_URL_KEY = Internal.createUniqueKey(Link.LINK, DSL.name("link_url_key"), new TableField[] { Link.LINK.URL }, true);
    public static final UniqueKey<SubscriptionRecord> SUBSCRIPTION_PKEY = Internal.createUniqueKey(Subscription.SUBSCRIPTION, DSL.name("subscription_pkey"), new TableField[] { Subscription.SUBSCRIPTION.CHAT_ID, Subscription.SUBSCRIPTION.LINK_ID }, true);

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<SubscriptionRecord, ChatRecord> SUBSCRIPTION__SUBSCRIPTION_CHAT_ID_FKEY = Internal.createForeignKey(Subscription.SUBSCRIPTION, DSL.name("subscription_chat_id_fkey"), new TableField[] { Subscription.SUBSCRIPTION.CHAT_ID }, Keys.CHAT_PKEY, new TableField[] { Chat.CHAT.ID }, true);
    public static final ForeignKey<SubscriptionRecord, LinkRecord> SUBSCRIPTION__SUBSCRIPTION_LINK_ID_FKEY = Internal.createForeignKey(Subscription.SUBSCRIPTION, DSL.name("subscription_link_id_fkey"), new TableField[] { Subscription.SUBSCRIPTION.LINK_ID }, Keys.LINK_PKEY, new TableField[] { Link.LINK.ID }, true);
}
