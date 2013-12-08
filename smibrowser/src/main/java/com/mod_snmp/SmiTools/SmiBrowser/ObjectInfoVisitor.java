package com.mod_snmp.SmiTools.SmiBrowser;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/


import com.mod_snmp.SmiParser.SyntaxTree.*;
import com.mod_snmp.SmiParser.Visitor.DepthFirstVisitor;

public class ObjectInfoVisitor extends DepthFirstVisitor {
    ObjectInfoView view;

    public ObjectInfoVisitor(ObjectInfoView view) {
        this.view = view;
    }

    /**
     * <PRE>
     * -> &lt;OBJECT_IDENTIFIER_T&gt;
     * </PRE>
     */
    public void visit(ObjectInfoObjectIdentifier n) {
        view.addLabel("OBJECT IDENTIFIER");
    }

    /**
     * <PRE>
     * -> &lt;OBJECT_TYPE_T&gt;
     * syntaxPart -> SyntaxPart()
     * unitsPart -> UnitsPart()
     * max_access -> AccessPart()
     * statusPart -> StatusPart()
     * descriptionPart -> DescriptionPart()
     * referencePart -> ReferencePart()
     * indexPart -> IndexPart()
     * defValPart -> DefValPart()
     * </PRE>
     */
    public void visit(ObjectInfoObjectType n) {
        view.addLabel("OBJECT-TYPE");
        n.syntaxPart.accept(this);
        n.unitsPart.accept(this);
        n.max_access.accept(this);
        n.statusPart.accept(this);
        n.descriptionPart.accept(this);
        n.referencePart.accept(this);
        n.indexPart.accept(this);
        n.defValPart.accept(this);
    }

    /**
     * <PRE>
     * -> &lt;MODULE_IDENTITY_T&gt;
     * lastUpdatedPart -> LastUpdatedPart()
     * organizationPart -> OrganizationPart()
     * contactInfoPart -> ContactInfoPart()
     * descriptionPart -> DescriptionPart()
     * revisions -> RevisionPart()
     * </PRE>
     */
    public void visit(ObjectInfoModuleIdentity n) {
        view.addLabel("MODULE-IDENTITY");
        n.lastUpdatedPart.accept(this);
        n.organizationPart.accept(this);
        n.contactInfoPart.accept(this);
        n.descriptionPart.accept(this);
        n.revisions.accept(this);
    }

    /**
     * <PRE>
     * -> &lt;OBJECT_IDENTITY_T&gt;
     * statusPart -> StatusPart()
     * descriptionPart -> DescriptionPart()
     * referencePart -> ReferencePart()
     * </PRE>
     */
    public void visit(ObjectInfoObjectIdentity n) {
        view.addLabel("OBJECT-IDENTITY");
        n.statusPart.accept(this);
        n.descriptionPart.accept(this);
        n.referencePart.accept(this);
    }

    /**
     * <PRE>
     * -> &lt;OBJECT_GROUP_T&gt;
     * objectsPart -> ObjectsPart()
     * statusPart -> StatusPart()
     * descriptionPart -> DescriptionPart()
     * referencePart -> ReferencePart()
     * </PRE>
     */
    public void visit(ObjectInfoObjectGroup n) {
        view.addLabel("OBJECT-GROUP");
        if (n.objectsPart.present()) {
            view.addLabel("OBJECTS");  
            view.addTextField(n.objectsPart.toCommaListString());
        } else {
            view.addLabel("OBJECTS");
            view.addTextField("");
        }
        n.statusPart.accept(this);
        n.descriptionPart.accept(this);
        n.referencePart.accept(this);
    }

    /**
     * <PRE>
     * -> &lt;NOTIFICATION_TYPE_T&gt;
     * objectsPart -> ObjectsPart()
     * statusPart -> StatusPart()
     * descriptionPart -> DescriptionPart()
     * referencePart -> ReferencePart()
     * </PRE>
     */
    public void visit(ObjectInfoNotificationType n) {
        view.addLabel("NOTIFICATION-TYPE");
        if (n.objectsPart.present()) {
            view.addLabel("OBJECTS");  
            view.addTextField(n.objectsPart.toCommaListString());
        } else {
            view.addLabel("OBJECTS");
            view.addTextField("");
        }
        n.statusPart.accept(this);
        n.descriptionPart.accept(this);
        n.referencePart.accept(this);
    }

    /**
     * <PRE>
     * -> &lt;NOTIFICATION_GROUP_T&gt;
     * notifications -> NotificationPart()
     * statusPart -> StatusPart()
     * descriptionPart -> DescriptionPart()
     * referencePart -> ReferencePart()
     * </PRE>
     */
    public void visit(ObjectInfoNotificationGroup n) {
        view.addLabel("NOTIFICATION-GROUP");
        view.addLabel("NOTIFICATIONS");
        view.addTextField(n.notifications.toCommaListString());
        n.statusPart.accept(this);
        n.descriptionPart.accept(this);
        n.referencePart.accept(this);
    }

    /**
     * <PRE>
     * -> &lt;MODULE_COMPLIANCE_T&gt;
     * statusPart -> StatusPart()
     * descriptionPart -> DescriptionPart()
     * referencePart -> ReferencePart()
     * moduleCompliancePart -> ( &lt;MODULE_T&gt; [ ModuleIdentifier() ]
     *                           [ MandatoryPart() ]
     *                           ( CompliancePart() )*
     *                          )+
     * </PRE>
     */
    public void visit(ObjectInfoModuleCompliance n) {
        view.addLabel("MODULE-COMPLIANCE");
        n.statusPart.accept(this);
        n.descriptionPart.accept(this);
        n.referencePart.accept(this);
        n.moduleCompliancePart.accept(this);
    }

    /**
     * <PRE>
     * -> &lt;AGENT_CAPABILITIES_T&gt;
     * productReleasePart -> ProductReleasePart()
     * statusPart -> StatusPart()
     * descriptionPart -> DescriptionPart()
     * referencePart -> ReferencePart()
     * moduleCapabilitiesPart -> ( ModuleCapability() )*
     * </PRE>
     */
    public void visit(ObjectInfoAgentCapabilities n) {
        view.addLabel("AGENT-CAPABILITIES");
        n.productReleasePart.accept(this);
        n.statusPart.accept(this);
        n.descriptionPart.accept(this);
        n.referencePart.accept(this);
        n.moduleCapabilitiesPart.accept(this);
    }

    /**
     * <PRE>
     * -> &lt;TRAP_TYPE_T&gt;
     * enterprisePart -> EnterprisePart()
     * variablesPart -> VariablesPart()
     * descriptionPart -> DescriptionPart()
     * number -> NumericValue()
     * </PRE>
     */
    public void visit(ObjectInfoTrapType n) {
        view.addLabel("TRAP-TYPE");
        n.enterprisePart.accept(this);
        n.variablesPart.accept(this);
        n.descriptionPart.accept(this);
        n.number.accept(this);
    }

    /**
     * <PRE>
     * nodeOptional -> [ &lt;LEFT_SQUARE_T&gt; ( &lt;APPLICATION_T&gt; | &lt;UNIVERSAL_T&gt; | &lt;PRIVATE_T&gt; ) &lt;DEC_NUMBER_T&gt; &lt;RIGHT_SQUARE_T&gt; [ &lt;EXPLICIT_T&gt; | &lt;IMPLICIT_T&gt; ] ]
     * </PRE>
     */
    public void visit(TypeTag n) {
        if (n.present()) {
            n.sort.accept(this);
            n.number.accept(this);
            n.kind.accept(this);
        }
        n.type.accept(this);
    }

    /**
     * Grammar production:
     * <PRE>
     * -> &lt;REVISION_T&gt; &lt;UTC_TIME_T&gt; DescriptionPart()     
     * </PRE>
     * Syntaxtree classes:
     * <PRE>
     * NodeToken time -> &lt;UTC_TIME_T&gt;
     * DescriptionPart descr -> DescriptionPart()   
     * </PRE>
     */
    public void visit(Revision n) {
        view.addLabel("REVISION");
        view.addTextField(n.time.toString());
        view.addTextField(n.descr.toString());
    }

    /**
     * <PRE>
     * -> &lt;STATUS_T&gt;
     * value -> ( &lt;CURRENT_T&gt;
     *          | &lt;DEPRECATED_T&gt;
     *          | &lt;OBSOLETE_T&gt;
     *          | &lt;MANDATORY_T&gt; )
     * </PRE>
     */
    public void visit(StatusPart n) {
        view.addLabel("Status");
        view.addTextField(n.value.toString());
    }

    /**
     * <PRE>
     * key -> ( &lt;ACCESS_T&gt;
     *        | &lt;MAX_ACCESS_T&gt;
     *        | &lt;MIN_ACCESS_T&gt; )
     * value -> ( &lt;NOT_ACCESSIBLE_T&gt;
     *          | &lt;ACCESSIBLE_FOR_NOTIFY_T&gt;
     *          | &lt;READ_ONLY_T&gt;
     *          | &lt;READ_WRITE_T&gt;
     *          | &lt;READ_CREATE_T&gt;
     *          | &lt;WRITE_ONLY_T&gt;
     *          | &lt;NOT_IMPLEMENTED_T&gt; )
     * </PRE>
     */
    public void visit(AccessPart n) {
        view.addLabel(n.key.toString());
        view.addTextField(n.value.toString());
    }

    /**
     * Grammar production:
     * <PRE>
     * -> &lt;MODULE_T&gt;
     * module_id -> [ ModuleIdentifier() ]
     * mandatory_part -> [ MandatoryPart() ]
     * compliance_part -> ( CompliancePart() )*
     * </PRE>
     */
    public void visit(ModuleCompliance n) {
        n.module_id.accept(this);
        n.mandatory_part.accept(this);
        n.compliance_part.accept(this);
    }

    /**
     * Grammar production:
     * <PRE>
     *  -> &lt;SUPPORTS_T&gt;
     * module_id -> ModuleIdentifier()
     * -> &lt;INCLUDES_T&gt;
     * -> &lt;LEFT_BRACE_T&gt;
     * object_list -> ObjectList()
     * -> &lt;RIGHT_BRACE_T&gt;
     * variation_part -> ( VariationPart() )*   
     * </PRE>
     */
    public void visit(ModuleCapability n) {
        view.addLabel("SUPPORTS");
        view.addTextField(n.module_id.toString());
        n.module_id.accept(this);  
        n.object_list.accept(this);
        n.variation_part.accept(this);
    }

    /**
     * <PRE>
     * -> &lt;VARIATION_T&gt;
     * identifier -> ValueIdentifier()
     * syntaxPart -> [ SyntaxPart() ]
     * write_syntax -> [ &lt;WRITE_SYNTAX_T&gt; Types() ]
     * access -> [ AccessPart() ]
     * creation_requires -> [ &lt;CREATION_REQUIRES_T&gt; &lt;LEFT_BRACE_T&gt; ObjectList() &lt;RIGHT_BRACE_T&gt; ]
     * defValPart -> DefValPart()
     * descriptionPart -> DescriptionPart()
     * </PRE>
     */
    public void visit(VariationPart n) {
        n.identifier.accept(this);
        n.syntaxPart.accept(this);
        n.write_syntax.accept(this);
        n.access.accept(this);
        n.creation_requires.accept(this);
        n.defValPart.accept(this);
        n.descriptionPart.accept(this);
    }

    /**
     * <PRE>
     * -> &lt;SYNTAX_T&gt;
     * type -> Types()
     * </PRE>
     */
    public void visit(SyntaxPart n) {
        n.type.accept(this);
    }

    /**
     * <PRE>
     * -> &lt;WRITE_SYNTAX_T&gt; Types()
     * type -> Types()
     * </PRE>
     */
    public void visit(WriteSyntaxPart n) {
        n.type.accept(this);
    }

    /**
     * <PRE>
     * -> &lt;GROUP_T&gt; ValueIdentifier() DescriptionPart()
     * identifier -> ValueIdentifier()
     * description -> DescriptionPart()
     * </PRE>
     */
    public void visit(ComplianceGroup n) {
        n.identifier.accept(this);
        n.description.accept(this);
    }
     
    /**
     * <PRE>
     * -> &lt;OBJECT_T&gt; ValueIdentifier()
     *       [ SyntaxPart() ]
     *       [ WriteSyntaxPart() ]
     *       [ AccessPart() ]
     *       DescriptionPart()
     * identifier -> ValueIdentifier()
     * syntax -> [ SyntaxPart() ]
     * write_syntax -> [ WriteSyntaxPart() ]
     * access -> [ AccessPart() ]
     * description -> DescriptionPart()
     * </PRE>
     */
    public void visit(ComplianceObject n) {
        n.identifier.accept(this);
        n.syntax.accept(this);
        n.write_syntax.accept(this);
        n.min_access.accept(this);
        n.description.accept(this);
    }

    /**
     * <PRE>
     * nodeChoice -> &lt;SEQUENCE_OF_T&gt; TypeIdentifier()
     *       | &lt;INTEGER_T&gt; [ IntegerRestriction() ]
     *       | &lt;INTEGER32_T&gt; [ IntegerRestriction() ]
     *       | &lt;UINTEGER32_T&gt; [ IntegerRestriction() ]
     *       | &lt;UNSIGNED32_T&gt; [ IntegerRestriction() ]
     *       | &lt;BITS_T&gt; [ RestrictionNamedNumberList() ]
     *       | &lt;BIT_STRING_T&gt; [ RestrictionNamedNumberList() ]
     *       | &lt;OCTET_STRING_T&gt; [ RestrictionSize() ]
     *       | &lt;OBJECT_IDENTIFIER_T&gt;
     *       | &lt;COUNTER_T&gt;
     *       | &lt;COUNTER32_T&gt;
     *       | &lt;COUNTER64_T&gt;
     *       | &lt;GAUGE_T&gt; [ IntegerRestriction() ]
     *       | &lt;GAUGE32_T&gt; [ IntegerRestriction() ]
     *       | &lt;TIMETICKS_T&gt;
     *       | &lt;OPAQUE_T&gt; [ RestrictionSize() ]
     *       | &lt;NSAP_ADDRESS_T&gt;
     *       | &lt;IP_ADDRESS_T&gt;
     *       | &lt;NETWORK_ADDRESS_T&gt;
     *       | TypeIdentifier() [ Restriction() ]
     * </PRE>
     */
    public void visit(TypeSmi n) {
        view.addLabel("SYNTAX-TYPE");
        view.addTextField(n.base.toString()
                                         + " " + n.restriction.toString());
    }

    /**
     * Grammar production:
     * <PRE>
     * -> &lt;AUGMENTS_T&gt;
     * -> &lt;LEFT_BRACE_T&gt;
     * identifier -> Identifier()
     * -> &lt;RIGHT_BRACE_T&gt; )           
     * </PRE>
     */
    public void visit(IndexAugments n) {
        view.addLabel("AUGMENTS");
        view.addTextField(n.identifier.toString());
    }

    /**
     * <PRE>
     *  -> [ ( &lt;INDEX_T&gt; &lt;LEFT_BRACE_T&gt; IndexTypes() &lt;RIGHT_BRACE_T&gt; ]
     * list -> Index() ( &lt;COMMA_T&gt; Index() )*
     * </PRE>
     */
    public void visit(IndexList n) {
        if (n.present()) {
            view.addLabel("INDEX");
            view.addTextField(n.list.toCommaListString(), 50);
        }
    }

    /**
     * <PRE>
     * nodeChoice -> &lt;DEC_NUMBER_T&gt;
     *       | &lt;BIN_NUMBER_T&gt;
     *       | &lt;HEX_NUMBER_T&gt;
     * </PRE>
     */
    public void visit(NumericValue n) {
        n.value.accept(this);
    }

    /**
     * <PRE>
     * -> &lt;DESCRIPTION_T&gt;
     * nodeToken1 -> &lt;TEXT_T&gt;
     * </PRE>
     */
    public void visit(DescriptionPart n) {
        view.addLabel("DESCRIPTION");
        view.addTextField(n.descr.toString());
    }

    /**
     * <PRE>
     * nodeOptional -> [ &lt;REFERENCE_T&gt; &lt;TEXT_T&gt; ]
     * </PRE>
     */
    public void visit(ReferencePart n) {
        view.addLabel("REFERENCE");
        view.addTextField(n.nodeOptional.toString());
    }

    /**
     * <PRE>
     * -> &lt;CONTACT_INFO_T&gt;
     * nodeToken1 -> &lt;TEXT_T&gt;
     * </PRE>
     */
    public void visit(ContactInfoPart n) {
        view.addLabel("CONTACT-INFO");
        view.addTextField(n.nodeToken.toString());
    }

    /**
     * <PRE>
     * -> &lt;ORGANIZATION_T&gt;
     * nodeToken1 -> &lt;TEXT_T&gt;
     * </PRE>
     */
    public void visit(OrganizationPart n) {
        view.addLabel("ORGANIZATION");
        view.addTextField(n.nodeToken.toString());
    }

    /**
     * <PRE>
     * value -> ( &lt;DEFVAL_T&gt; &lt;LEFT_BRACE_T&gt;
     *               (  NumericValue()
     *               | ValueIdentifier()
     *               | &lt;TEXT_T&gt;
     *               | BitValueList()
     *               | AssignedValue()
     *               )
     *            &lt;RIGHT_BRACE_T&gt; )?
     * </PRE>
     */
    public void visit(DefValPart n) {
        view.addLabel("DEFVAL");
        view.addTextField(n.nodeOptional.toString());
    }

    /**
     * <PRE>
     * nodeOptional -> [ &lt;DISPLAY_HINT_T&gt; &lt;TEXT_T&gt; ]
     * </PRE>
     */
    public void visit(DisplayHintPart n) {
        view.addLabel("DISPLAY-HINT");
        view.addTextField(n.display_hint.toString());
    }

    /**
     * <PRE>
     * -> &lt;LAST_UPDATED_T&gt;
     * nodeToken1 -> &lt;UTC_TIME_T&gt;
     * </PRE>
     */
    public void visit(LastUpdatedPart n) {
        view.addLabel("LAST-UPDATED");
        view.addTextField(n.nodeToken.toString());
    }

    /**
     * <PRE>
     * nodeOptional -> [ &lt;UNITS_T&gt; &lt;TEXT_T&gt; ]
     * </PRE>
     */
    public void visit(UnitsPart n) {
        view.addLabel("UNITS");
        view.addTextField(n.nodeOptional.toString());
    }

    /**
     * <PRE>
     * -> &lt;PRODUCT_RELEASE_T&gt;
     * nodeToken1 -> &lt;TEXT_T&gt;
     * </PRE>
     */
    public void visit(ProductReleasePart n) {
        view.addLabel("PRODUCT-RELEASE");
        view.addTextField(n.nodeToken.toString());
    }

    /**
     * <PRE>
     * -> &lt;ENTERPRISE_T&gt;
     * identifier -> ValueIdentifier()
     * </PRE>
     */
    public void visit(EnterprisePart n) {
        n.identifier.accept(this);
    }

    /**
     * Grammar production:
     * <PRE>
     * identifier -> ValueIdentifier()
     * type -> Types()
     * </PRE>
     */
    public void visit(ValueType n) {
        n.identifier.accept(this);
        n.type.accept(this);
    }

}
