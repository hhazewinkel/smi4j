package com.mod_snmp.smiparser.syntaxtree;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/


/**
 * Grammar production:
 * <PRE>
 * notifications -> NotificationPart()
 * statusPart -> StatusPart()
 * descriptionPart -> DescriptionPart()
 * referencePart -> ReferencePart()
 * </PRE>
 */
public class ObjectInfoNotificationGroup implements ObjectInfo {
    private Node parent;
    private int level = 0;
    public NodeList notifications;
    public StatusPart statusPart;
    public DescriptionPart descriptionPart;
    public ReferencePart referencePart;

    public ObjectInfoNotificationGroup(NodeList n1,
                                                StatusPart n2,
                                                DescriptionPart n3,
                                                ReferencePart n4) {
        notifications = n1;
        if ( notifications != null ) notifications.setParent(this);
        statusPart = n2;
        if ( statusPart != null ) statusPart.setParent(this);
        descriptionPart = n3;
        if ( descriptionPart != null ) descriptionPart.setParent(this);
        referencePart = n4;
        if ( referencePart != null ) referencePart.setParent(this);
    }
    public int line() {
        return notifications.line();
    }
    public boolean isConceptualRow() {
        return false;
    }
    public void setIndexLevel(int i) {
    }
    public int getIndexLevel() {
        return 0;
    }
    public void accept(com.mod_snmp.smiparser.visitor.Visitor v) {
        v.visit(this);
    }
    public Object accept(com.mod_snmp.smiparser.visitor.ObjectVisitor v, Object argu) {
        return v.visit(this,argu);
    }
    public void setParent(Node n) { parent = n; }
    public Node getParent()       { return parent; }
}

