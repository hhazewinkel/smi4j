/**
 * SemanticsCheck
 * Class that walks thru the syntaxtree to do semantical checking.
 * Before this phase the semantics check needs to be setup.
 * The setup is mainly to insert all assignments in a symbol table.
 */
package com.mod_snmp.SmiParser.Semantics;

import com.mod_snmp.SmiParser.ErrorHandler.ErrorException;
import com.mod_snmp.SmiParser.ErrorHandler.Message;
import com.mod_snmp.SmiParser.ErrorHandler.SmiException;
import com.mod_snmp.SmiParser.SyntaxTree.*;
import com.mod_snmp.SmiParser.Visitor.DepthFirstVisitor;
import com.mod_snmp.SmiParser.Visitor.Visitor;

import java.util.Enumeration;

public class SemanticsCheck extends DepthFirstVisitor implements Visitor {
    private static SymbolHashTable current;

    public void visit(ModuleDefinition n) {
        try {
            current = ModuleHashTable.lookup(n.moduleIdentifier);
        } catch (SmiException exception) {
            Message.notice(n.moduleIdentifier, "module is not loaded");
        }
        checkVariablesDefined(n.exports_list, current,
                                "exported, but not defined in this module, "
                                                        + n.moduleIdentifier);
        n.import_module_list.accept(this);
        n.assignmentList.accept(this);
    }

    /**
     * <PRE>
     * PredefinedMacro() -> (&lt;OBJECT_TYPE_T&gt;        | &lt;OBJECT_IDENTITY_T&gt;
     *                     | &lt;OBJECT_GROUP_T&gt;       | &lt;TEXTUAL_CONVENTION_T&gt;
     *                     | &lt;NOTIFICATION_TYPE_T&gt;  | &lt;NOTIFICATION_GROUP_T&gt;
     *                     | &lt;MODULE_IDENTITY_T&gt;    | &lt;MODULE_COMPLIANCE_T&gt;
     *                     | &lt;AGENT_CAPABILITIES_T&gt; | &lt;TRAP_TYPE_T&gt; )
     * SmiTypes() -> ( &lt;IP_ADDRESS_T&gt;   | &lt;NETWORK_ADDRESS_T&gt;      
     *               | &lt;NSAP_ADDRESS_T&gt; | &lt;COUNTER_T&gt;               
     *               | &lt;COUNTER32_T&gt;    | &lt;COUNTER64_T&gt;     
     *               | &lt;GAUGE_T&gt;        | &lt;GAUGE32_T&gt; 
     *               | &lt;TIMETICKS_T&gt;    | &lt;OPAQUE_T&gt;     
     *               | &lt;INTEGER_T&gt;      | &lt;INTEGER32_T&gt;   
     *               | &lt;UINTEGER32_T&gt;   | &lt;UNSIGNED32_T&gt; )      
     * Symbol() -> ( TypeIdentifier() | ValueIdentifier()      
     *       | SmiTypes() | PredefinedMacro() )      
     *
     * symbolList -> Symbol() (&lt;COMMA&gt; Symbol() )*
     * -> &lt;FROM_T&gt;
     * moduleIdentifier -> ModuleIdentifier()
     * </PRE>
     */
    public void visit(ModuleImport n) {
        try {
            SymbolHashTable table = ModuleHashTable.lookup(n.moduleIdentifier);
            
            Enumeration implist = n.symbolList.elements();
            while (implist.hasMoreElements()) {
                Identifier id = (Identifier)implist.nextElement();
                if (! table.exists(id)) {
                    Message.warning(id,
                                "imported, but not defined in module, "
                                                        + n.moduleIdentifier);
                }
                if (id.getKind() == Identifier.TYPE) {
                    try {
                        Type type = (Type)table.lookup(id);
                        current.replace(id, type.getGenericType());
                    } catch (ErrorException exception) {
                        Message.error(id, exception.getMessage()
                                                       + " in symbol table");
                    } catch (ClassCastException exception) {
                        /* Actually if this happens, there is a real problem. */
                        System.err.println("Class casting error ");
                    }
                }
            }
        } catch (SmiException exception) {
            Message.notice(n.moduleIdentifier, "module is not loaded");
            /* See what we can fix for later. */
            Enumeration implist = n.symbolList.elements();
            while (implist.hasMoreElements()) {
                Identifier id = (Identifier)implist.nextElement();
                if (id.getKind() == Identifier.TYPE) {
                    try {
                        TypeIdentifier dummy_type = new TypeIdentifier(id);
                        dummy_type.setGenericType(dummy_type);
                        current.replace(id, dummy_type);
                    } catch (ErrorException utils_exception) {
                    }
                }
            }
        }
    }

    /**
     * <PRE>
     * n.identifier -> TypeIdentifier()
     * -> &lt;ASSIGN_T&gt;
     * type -> Types()
     * </PRE>
     * Check for the correct type semantics.
     */      
    public void visit(AssignmentType n) {
        n.type.accept(this);
    }

    /**      
     * Grammar production:
     * <PRE> 
     * -> &lt;SEQUENCE_T&gt;
     *    &lt;L_BRACE_T&gt;
     *    ValueType() ( &lt;COMMA_T&gt; ValueType() )* 
     *    &lt;R_BRACE_T&gt;
     * vtList -> ( ValueType() )*
     * </PRE>
     */
    public void visit(TypeSequence n) {
        n.vtList.accept(this);
    }

   /**
     * <PRE>
     * -> &lt;TEXTUAL_CONVENTION_T&gt;
     * displayhint -> DisplayHintPart();             
     * status -> StatusPart();
     * description -> DescriptionPart(); 
     * reference -> ReferencePart();   
     * syntax -> SyntaxPart();
     * </PRE>
     */
    public void visit(TypeTextualConvention n) {
        n.syntax.accept(this);
    }

    /**
     * <PRE>
     * ->  &lt;CHOICE_T&gt;
     * choice ->  ChoiceValue()
     * </PRE>
     */
    public void visit(TypeChoice n) {
    }

    /**         
     * <PRE>
     * -> [
     *     -> &lt;LEFT_SQUARE_T&gt;
     *     sort -> &lt;APPLICATION_T&gt; | &lt;UNIVERSAL_T&gt; | &lt;PRIVATE_T&gt; 
     *     number -> &lt;DEC_NUMBER_T&gt;
     *     -> &lt;RIGHT_SQUARE_T&gt;
     *     kind -> [ &lt;EXPLICIT_T&gt; | &lt;IMPLICIT_T&gt; ]
     *     ]
     * type -> Types() | Asn1Types()
     * </PRE>   
     */     
    public void visit(TypeTag n) {
    }

    /** 
     * <PRE>
     * identifier -> TypeIdentifier()
     * restriction -> [ Restriction() ]
     * </PRE>
     */
    public void visit(TypeIdentifier n) {
        try {
            Type tp = (Type)current.lookup(n.identifier);
            n.setGenericType(tp.getGenericType());
        } catch (ErrorException exception) {
            Message.warning(n.identifier,
                       "not defined or imported in this module");
        } catch (ClassCastException exception) {
            Message.alert(n.identifier,
                       "Internal error");
        }
    }       
     
    /**
     * <PRE> 
     * -> &lt;SEQUENCE_OF_T&gt;
     * identifier -> TypeIdentifier()
     * </PRE>
     */
    public void visit(TypeSequenceOf n) {
        if (! current.exists(n.identifier.identifier)) {      
            Message.warning(n.identifier,
                       "not defined or imported in this module");         
        }
    }

    /**
     * <PRE>
     * identifier -> PredefinedMacros()  smi_defined == true
     *               |
     *               TypeIdentifier()    smi_defined == false
     * macro_token -> &lt;MACRO_T&gt;
     * -> &lt;ASSIGN_T&gt;
     * begin_token -> &lt;BEGIN_T&gt;
     * macro -> MacroBody()      
     * end-> &lt;END_T&gt;
     * </PRE>
     */
    public void visit(AssignmentMacro n) {
    }

    /**
     * <PRE>
     * identifier -> ValueIdentifier()
     * info -> ObjectInfo()
     * -> &lt;ASSIGN_T&gt;
     * assignedValue -> AssignedValue()
     * </PRE>
     */
    public void visit(AssignmentObject n) {
        try {
            checkAssignedValue(n.identifier, n.assignedValue);
        } catch (SemanticsException exception) {
            Message.error(n.identifier, exception.getMessage());
        }
        n.info.accept(this);
    }

    /**
     * <PRE>
     * -> &lt;OBJECT_IDENTIFIER_T&gt;
     * </PRE>
     * Nothing to check.
     */
    public void visit(ObjectInfoObjectIdentifier n) {
    }

    /**
     * <PRE>
     * -> Arbitrary not parsed stuff
     * </PRE>
     */
    public void visit(MacroBody n) {
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
     * Semantics:
     * - Check if the OBJECT-TYPE is imported.
     * - Test the SYNTAX type.
     * - Test if the generic type is allowed in the combination:
     *   'Counter' and 'DEFVAL'
     *   'Counter' and 'MAX-ACCESS'
     */
    public void visit(ObjectInfoObjectType n) {
        if (! current.exists("OBJECT-TYPE")) {
            Message.warning(n.line(), "'OBJECT-TYPE' not imported");
            try {
                current.insert("OBJECT-TYPE", n);
            } catch (ErrorException exception) {
            }
        }
        n.syntaxPart.accept(this);
        int accesstype = n.max_access.getAccessType();
        int accessvalue = n.max_access.getAccessValue();
        if (accesstype != AccessPart.MAX_ACCESS) {
            Message.error(n.max_access.key, "must be 'MAX-ACCESS'");
        }
        if ((accessvalue == AccessPart.NOT_IMPLEMENTED) ||
            (accessvalue == AccessPart.WRITE_ONLY)) {
            Message.error(n.max_access.value, "is not allowed for MAX-ACCESS-clause");
        }
        Type tp = n.syntaxPart.getGenericType();
        if (tp instanceof TypeSmi) {
            int kind = ((TypeSmi)tp).getKind();
            if (n.defValPart.present() && ((kind == TypeSmi.COUNTER) ||
                                           (kind == TypeSmi.COUNTER32) ||
                                           (kind == TypeSmi.COUNTER64))) {
                Message.error(n.defValPart.line(), "DEF-VAL not allowed with '" + tp + "' type");
            }
            if (((accessvalue != AccessPart.READ_ONLY) &&
                 (accessvalue != AccessPart.ACCESSIBLE_FOR_NOTIFY)) &&
                                           ((kind == TypeSmi.COUNTER) ||
                                            (kind == TypeSmi.COUNTER32) ||
                                            (kind == TypeSmi.COUNTER64))) {
                Message.error(n.max_access,
                                    "access not allowed with syntax '" + tp + "' type" +
                                    "\n\tmay only be 'read-only' or 'accessible-for-notify'");
            }
        }
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
     * Semantics:
     * - test if MODULE-IDENTIY is imported.
     */
    public void visit(ObjectInfoModuleIdentity n) {
        if (! current.exists("MODULE-IDENTITY")) {
            Message.warning(n.line(), "'MODULE-IDENTITY' not imported");
            try {
                current.insert("MODULE-IDENTITY", n);
            } catch (ErrorException exception) {
            }
        }
        if (checkRevisions(n.revisions, n.lastUpdatedPart)) {
            Message.warning(n.lastUpdatedPart,
                                        "update time is not added to revision list");
        }
    }

    /**
     * <PRE>
     * lastUpdate -> &lt;UTC_TIME_T&gt;
     * revisions -> ( &lt;REVISION_T&gt; &lt;UTC_TIME_T&gt; DescriptionPart() )*
     * </PRE>
     * checkRevisions
     * Checks if the LAST-UPDATED value is specified within the
     * list of revisions.
     */
    public boolean checkRevisions(NodeList revisions, LastUpdatedPart last_update) {
        String updated = last_update.toString();
        for (int i = 0 ; i < revisions.size() ; i++) {
            Revision revision = (Revision)revisions.elementAt(i);
            if (updated.equals(revision.time.toString())) {
                return false;
            }
        }
        return true;
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
        if (! current.exists("OBJECT-IDENTITY")) { 
            Message.warning(n.line(), "'OBJECT-IDENTITY' not imported");
            try {
                current.insert("OBJECT-IDENTITY", n);
            } catch (ErrorException exception) {
            }
        }
    }

    /**
     * <PRE>
     * -> &lt;OBJECT_GROUP_T&gt;
     * objectsPart -> ObjectsPart()
     *             -> [ &lt;OBJECTS_T&gt; &lt;LEFT_BRACE_T&gt;
     *                  ObjectList()
     *                  &lt;RIGHT_BRACE_T&gt; ]
     * statusPart -> StatusPart()
     * descriptionPart -> DescriptionPart()
     * referencePart -> ReferencePart()
     * </PRE>
     */
    public void visit(ObjectInfoObjectGroup n) {
        if (! current.exists("OBJECT-GROUP")) { 
            Message.warning(n.line(), "'OBJECT-GROUP' not imported");
            try {
                current.insert("OBJECT-GROUP", n);
            } catch (ErrorException exception) {
            }
        }
        checkVariablesDefined(n.objectsPart);
    }

    /**
     * <PRE>
     * -> &lt;NOTIFICATION_TYPE_T&gt;
     * objectsPart -> ObjectsPart()
     *             -> [ &lt;OBJECTS_T&gt; &lt;LEFT_BRACE_T&gt;
     *                  ObjectList()
     *                  &lt;RIGHT_BRACE_T&gt; ]
     * statusPart -> StatusPart()
     * descriptionPart -> DescriptionPart()
     * referencePart -> ReferencePart()
     * </PRE>
     */
    public void visit(ObjectInfoNotificationType n) {
        if (! current.exists("NOTIFICATION-TYPE")) { 
            Message.warning(n.line(), "'NOTIFICATION-TYPE' not imported");
            try {
                current.insert("NOTIFICATION-TYPE", n); 
            } catch (ErrorException exception) {
            }
        }
        checkVariablesDefined(n.objectsPart);
    }

    /**
     * <PRE>
     * -> &lt;NOTIFICATION_GROUP_T&gt;
     * notificationPart -> NotificationPart()
     *               -> &lt;NOTIFICATIONS_T&gt;
     *                  &lt;LEFT_BRACE_T&gt;
     *                  ObjectList()
     *                  &lt;RIGHT_BRACE_T&gt;
     * statusPart -> StatusPart()
     * descriptionPart -> DescriptionPart()
     * referencePart -> ReferencePart()
     * </PRE>
     */
    public void visit(ObjectInfoNotificationGroup n) {
        if (! current.exists("NOTIFICATION-GROUP")) { 
            Message.warning(n.line(), "'NOTIFICATION-GROUP' not imported");
            try {
                current.insert("NOTIFICATION-GROUP", n);
            } catch (ErrorException exception) {
            }
        }
        checkVariablesDefined(n.notifications);
    }

    /**
     * <PRE>
     * -> &lt;MODULE_COMPLIANCE_T&gt;
     * statusPart -> StatusPart()
     * descriptionPart -> DescriptionPart()
     * referencePart -> ReferencePart()
     * moduleCompliancePart -> ( ModuleCompliance() )+
     * </PRE>
     */
    public void visit(ObjectInfoModuleCompliance n) {
        if (! current.exists("MODULE-COMPLIANCE")) { 
            Message.warning(n.line(), "'MODULE-COMPLIANCE' not imported");
            try {
                current.insert("MODULE-COMPLIANCE", n);
            } catch (ErrorException exception) {
            }
        }
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
        if (! current.exists("AGENT-CAPABILITIES")) {
            Message.warning(n.line(), "'AGENT-CAPABILITIES' not imported");
            try {
                current.insert("AGENT-CAPABILITIES", n);
            } catch (ErrorException exception) {
            }
        }
        n.moduleCapabilitiesPart.accept(this);
    }

    /**
     * <PRE>
     * -> &lt;TRAP_TYPE_T&gt;
     * enterprisePart -> EnterprisePart()
     * variablesPart -> VariablesPart()
     *               -> [ &lt;VARIABLES_T&gt;
     *                    &lt;LEFT_BRACE_T&gt;
     *                    ObjectList()
     *                    &lt;RIGHT_BRACE_T&gt; ]
     * descriptionPart -> DescriptionPart()
     * number -> NumericValue()
     * </PRE>
     */
    public void visit(ObjectInfoTrapType n) {
        if (! current.exists("TRAP-TYPE")) {
            Message.warning(n.line(), "'TRAP-TYPE' not imported");
            try {
                current.insert("TRAP-TYPE", n);
            } catch (ErrorException exception) {
            }
        }
        //n.enterprisePart.accept(this);
        checkVariablesDefined(n.variablesPart);
        //n.number.accept(this);
    }

    /**
     * <PRE>
     * -> &lt;STATUS_T&gt;
     * nodeChoice -> ( &lt;CURRENT_T&gt; | &lt;DEPRECATED_T&gt; | &lt;OBSOLETE_T&gt; | &lt;MANDATORY_T&gt; )
     * </PRE>
     */
    public void visit(StatusPart n) {
        n.value.accept(this);
    }

    /**
     * Grammar production:
     * <PRE>
     * -> &lt;MODULE_T&gt;
     *    [ ModuleIdentifier() ]
     *    [ &lt;MANDATORY_GROUPS_T&gt;
     *      &lt;LEFT_BRACE_T&gt;
     *      ObjectList()
     *      &lt;RIGHT_BRACE_T&gt;
     *    ]
     *   ( CompliancePart() )*
     * </PRE>
     * syntax class variables:
     * <PRE>
     * module_id -> [ ModuleIdentifier() ]
     * mandatory_part -> [ ObjectList() ]
     * compliance_part -> ( CompliancePart() )*
     * </PRE>
     */
    public void visit(ModuleCompliance n) {
        try {
            SymbolHashTable table;
            String msg;
            if (n.module_id.present()) {
                msg = "not defined in this module '" + n.module_id + "'";
                table = ModuleHashTable.lookup(n.module_id.toString());
                checkVariablesDefined(((NodeList)n.mandatory_part.node),
                      ModuleHashTable.lookup(n.module_id.toString()),
                      msg);
            } else {
                msg = "not defined in this module";
                table = current;
                checkVariablesDefined(((NodeList)n.mandatory_part.node));
            }
            if (n.compliance_part.present()) {
                Enumeration e = n.compliance_part.elements();
                while (e.hasMoreElements()) {
                    CompliancePart compliance = (CompliancePart)e.nextElement();
                    if (! table.exists(compliance.getIdentifier())) {
                        Message.warning(compliance, msg);
                    }
                }
            }
        } catch (SmiException exception) {
            Message.error(n.module_id, "module is not loaded");
        }
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
        try {
            checkVariablesDefined(n.object_list,
                            ModuleHashTable.lookup(n.module_id.toString()),
                            "not defined in this module '" + n.module_id + "'");
        } catch (SmiException exception) {
            Message.error(n.module_id, "module is not loaded");
        }
        //n.variation_part.accept(this);
    }   

    /** 
     * <PRE>
     * -> &lt;VARIATION_T&gt;
     * identifier -> ValueIdentifier()
     * syntaxPart -> SyntaxPart()
     * write_syntax -> [ &lt;WRITE_SYNTAX_T&gt; Types() ]
     * access -> [ AccessPart() ]
     * creation_requires -> [ &lt;CREATION_REQUIRES_T&gt; &lt;LEFT_BRACE_T&gt; ObjectList() &lt;RIGHT_BRACE_T&gt; ]
     * defValPart -> DefValPart()
     * descriptionPart -> DescriptionPart()
     * </PRE>
     */
    public void visit(VariationPart n) {
        try {
            AssignmentObject obj = (AssignmentObject)current.lookup(n.identifier);
            if (obj.info instanceof ObjectInfoObjectType) {
            } else if (obj.info instanceof ObjectInfoNotificationType) {
                if (n.syntaxPart.present()) {
                    Message.error(n.syntaxPart,
                             "not allowed for VARIATION of NOTIFICATION-TYPE" +
                                                             n.identifier);
                }
                if (n.write_syntax.present()) {
                    Message.error(n.write_syntax,
                             "not allowed for VARIATION of NOTIFICATION-TYPE" +
                                                             n.identifier);
                }
                if (n.access.present()) {
                    int accesstype = ((AccessPart)n.access.node).getAccessType();
                    int accessvalue = ((AccessPart)n.access.node).getAccessValue();
                    if (accesstype != AccessPart.ACCESS) {
                        Message.error(((AccessPart)n.access.node).key, "must be 'ACCESS'");
                    }   
                    if ((accessvalue == AccessPart.NOT_IMPLEMENTED) ||
                        (accessvalue == AccessPart.WRITE_ONLY)) {
                        Message.error(((AccessPart)n.access.node).value, "is not allowed for ACCESS-clause");
                    }
                }       
                if (n.creation_requires.present()) {
                    Message.error(n.creation_requires,
                             "not allowed for VARIATION of NOTIFICATION-TYPE" +
                                                             n.identifier);
                }
                if (n.defValPart.present()) {
                    Message.error(n.defValPart,
                             "not allowed for VARIATION of NOTIFICATION-TYPE" +
                                                             n.identifier);
                }
                n.descriptionPart.accept(this);
            } else {
                Message.error(n.identifier, "identifier is not allowed in a VARIATION-clause");
            }
        } catch (SmiException exception) {
        }
    }

    /**
     * <PRE>
     * -> &lt;SYNTAX_T&gt;
     * types -> Types()
     * </PRE>
     */
    public void visit(SyntaxPart n) {
        n.type.accept(this);
    }
        
    /** 
     * <PRE>
     * -> &lt;WRITE_SYNTAX_T&gt;
     * types -> Types()
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

        /* The ACCESS clause */
        if (n.min_access.present()) {
            int accesstype = ((AccessPart)n.min_access.node).getAccessType();
            int accessvalue = ((AccessPart)n.min_access.node).getAccessValue();
            if (accesstype != AccessPart.MIN_ACCESS) {
                Message.error(((AccessPart)n.min_access.node).key, "must be 'MIN-ACCESS'");
            }   
            if (accessvalue == AccessPart.NOT_ACCESSIBLE) {
                Message.error(((AccessPart)n.min_access.node).value, "is not allowed for MIN-ACCESS-clause");
            }
        }

        n.description.accept(this);
    }

    /**
     * <PRE>
     * nodeChoice ->
     *       | &lt;INTEGER_T&gt; [ RestrictionNamedNumberList | RestrictionRange() ]
     *       | &lt;INTEGER32_T&gt; [ RestrictionNamedNumberList | RestrictionRange() ]
     *       | &lt;UINTEGER32_T&gt; [ RestrictionNamedNumberList | RestrictionRange() ]
     *       | &lt;UNSIGNED32_T&gt; [ RestrictionNamedNumberList | RestrictionRange() ]
     *       | &lt;BITS_T&gt; [ RestrictionNamedNumberList() ]
     *       | &lt;BIT_STRING_T&gt; [ RestrictionNamedNumberList() ]
     *       | &lt;OCTET_STRING_T&gt; [ RestrictionSize() ]
     *       | &lt;OBJECT_IDENTIFIER_T&gt;
     *       | &lt;COUNTER_T&gt;
     *       | &lt;COUNTER32_T&gt;
     *       | &lt;COUNTER64_T&gt;
     *       | &lt;GAUGE_T&gt; [ RestrictionNamedNumberList | RestrictionRange() ]
     *       | &lt;GAUGE32_T&gt; [ RestrictionNamedNumberList | RestrictionRange() ]
     *       | &lt;TIMETICKS_T&gt;
     *       | &lt;OPAQUE_T&gt; [ RestrictionSize() ]
     *       | &lt;NSAP_ADDRESS_T&gt;
     *       | &lt;IP_ADDRESS_T&gt;
     *       | &lt;NETWORK_ADDRESS_T&gt;
     * </PRE>
     */
    public void visit(TypeSmi n) {
        n.base.accept(this);
        n.restriction.accept(this);
    }

    /**
     * <PRE>
     * -> &lt;LEFT_PAREN_T&gt;
     * range -> Range()
     * -> &lt;RIGHT_PAREN_T&gt;
     * </PRE>
     */
    public void visit(RestrictionRange n) {
        n.range.accept(this);
    }

    /**
     * <PRE>
     * -> &lt;LEFT_PAREN_T&gt;
     * -> &lt;SIZE_T&gt;
     * -> &lt;LEFT_PAREN_T&gt;
     * range -> Range()
     * -> &lt;RIGHT_PAREN_T&gt;
     * -> &lt;RIGHT_PAREN_T&gt;
     * </PRE>
     */
    public void visit(RestrictionSize n) {
        n.range.accept(this);
    }

    /**
     * <PRE>
     * -> &lt;LEFT_BRACE_T&gt;
     * list -> NamedNumber() ( &lt;COMMA_T&gt; NamedNumber() )*
     * -> &lt;RIGHT_BRACE_T&gt;
     * </PRE>
     */
    public void visit(RestrictionNamedNumberList n) {
        n.list.accept(this);
        if (n.list.present()) {
            long first = ((NamedNumber)n.list.firstElement()).getValue();
            if (first != 1) {
                Message.warning((NamedNumber)n.list.firstElement(),
                                        "named number list does not start with 1");
            }
            for (int i = 1 ; i < n.list.size() ; i++) {
                long current = ((NamedNumber)n.list.elementAt(i)).getValue();
                if (++first != current) {
                    first = current;
                    Message.warning((NamedNumber)n.list.elementAt(i),
                                        "named number list is not monotonically increasing");
                }
            }
        }
    }

    /**
     * <PRE>
     * list-> RangeItem() ( &lt;OR_T&gt; RangeItem() )*
     * </PRE>
     */
    public void visit(RangeList n) {
        /* Test if all individual RangeItems are correct */
        n.list.accept(this);
        /* Test if the RangeItem list is correct. */
        for (int i = 1; i < n.list.size() ; i++) {
            checkRangeOrder((RangeItem)n.list.elementAt(i-1),
                            (RangeItem)n.list.elementAt(i));
        }
    }
    private void checkRangeOrder(RangeItem first, RangeItem last) {
       if (first.getLastValue() > last.getFirstValue()) {
            Message.warning(last, "range order not increasing or overlapping");
        }
    }

    /**     
     * <PRE>
     * -> NumericValue() |
     *    NumericValue() &lt;UNTIL_T&gt; NumericValue()
     * begin -> NumericValue()  
     * end -> NumericValue() -- NodeOptional --
     * </PRE>
     */  
    public void visit(RangeItem n) {
        if (n.end.present() && (n.getFirstValue() > n.getLastValue())) {
            Message.warning(n.begin, "range start higher then it ends");
        }
    }   

    /**
     * <PRE>
     * identifier -> ValueIdentifier()
     * -> &lt;LEFT_PAREN_T&gt;
     * number -> NumericValue()
     * -> &lt;RIGHT_PAREN_T&gt;
     * </PRE>
     */
    public void visit(NamedNumber n) {
        String name = n.name.toString();
        if (name.indexOf('-') >= 0) {
            Message.warning(n.name, "contains a not allowed hyphen, '-'");
        }
    }

    /**
     * <PRE>
     * nodeChoice -> ( ModuleIdentifier() &lt;DOT_T&gt; &lt;TYPE_IDENTIFIER_T&gt; | &lt;TYPE_IDENTIFIER_T&gt; )
     * nodeChoice -> ( ModuleIdentifier() &lt;DOT_T&gt; ( &lt;VALUE_IDENTIFIER_T&gt; | &lt;NOT_IMPLEMENTED_T&gt; | &lt;CURRENT_T&gt; | &lt;DEPRECATED_T&gt; | &lt;OBSOLETE_T&gt; | &lt;MANDATORY_T&gt; ) | ( &lt;VALUE_IDENTIFIER_T&gt; | &lt;NOT_IMPLEMENTED_T&gt; | &lt;CURRENT_T&gt; | &lt;DEPRECATED_T&gt; | &lt;OBSOLETE_T&gt; | &lt;MANDATORY_T&gt; ) )
     * </PRE>
     */
    public void visit(Identifier n) {
        if (n.module_identifier != null) {
            n.module_identifier.accept(this);
        }
        n.identifier.accept(this);
    }

    /**
     * <PRE>
     * nodeToken -> &lt;TYPE_IDENTIFIER_T&gt;
     * </PRE>
     */
    public void visit(ModuleIdentifier n) {
        n.nodeToken.accept(this);
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
        n.identifier.accept(this);
    }

    /** 
     * Grammar production:
     * <PRE>
     * -> &lt;IMPLIED_T&gt;
     * identifier -> Identifier()
     * </PRE>
     */
    public void visit(Index n) {
    }


    /**
     * <PRE>
     * list -> Index() ( &lt;COMMA_T&gt; Index() )*
     * </PRE>
     */
    public void visit(IndexList n) {
        if ( n.list.present() ) {
            int i = 1;
            Enumeration e = n.list.elements();
            while (e.hasMoreElements()) {
                Index item = (Index)e.nextElement();
                try {
                    AssignmentObject obj = (AssignmentObject)current.lookup(item.identifier);
                    obj.info.setIndexLevel(i++);
                } catch (SmiException exception) {
                    Message.error(item.identifier,
                               "not defined or imported in this module");
                }
            }
        }
    }

    /**
     * <PRE>
     * -> &lt;LEFT_BRACE_T&gt;
     * oidValue -> OidValue() ( OidValue() )+
     * -> &lt;RIGHT_BRACE_T&gt;
     * </PRE>
     */
    public void visit(AssignedValue n) {
        n.oidValue.accept(this);
    }

    /**
     * <PRE>
     * nodeChoice -> ValueIdentifier() [ &lt;LEFT_PAREN_T&gt; NumericValue() &lt;RIGHT_PAREN_T&gt; ]
     *       | NumericValue()
     * </PRE>
     */
    public void visit(OidValue n) {
        if (n.hasName()) {
            n.name.accept(this);
            if (n.hasNumber()) {
                n.numval.accept(this);
            }
        } else if (n.hasNumber()) {
            n.numval.accept(this);
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
        n.descr.accept(this);
    }

    /**
     * <PRE>
     * nodeOptional -> [ &lt;REFERENCE_T&gt; &lt;TEXT_T&gt; ]
     * </PRE>
     */
    public void visit(ReferencePart n) {
        n.nodeOptional.accept(this);
    }

    /**
     * <PRE>
     * -> &lt;CONTACT_INFO_T&gt;
     * nodeToken1 -> &lt;TEXT_T&gt;
     * </PRE>
     */
    public void visit(ContactInfoPart n) {
        n.nodeToken.accept(this);
    }

    /**
     * <PRE>
     * -> &lt;ORGANIZATION_T&gt;
     * nodeToken1 -> &lt;TEXT_T&gt;
     * </PRE>
     */
    public void visit(OrganizationPart n) {
        n.nodeToken.accept(this);
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
        n.nodeOptional.accept(this);
    }

    /**
     * <PRE>
     * list -> ( ValueIdentifier() )*
     * </PRE>
     */
    public void visit(BitValueList n) {
        n.list.accept(this);
    }

    /**
     * <PRE>
     * nodeOptional -> [ &lt;DISPLAY_HINT_T&gt; &lt;TEXT_T&gt; ]
     * </PRE>
     */
    public void visit(DisplayHintPart n) {
        if (n.present()) {
            n.display_hint.accept(this);
        }
    }

    /**
     * <PRE>
     * -> &lt;LAST_UPDATED_T&gt;
     * nodeToken1 -> &lt;UTC_TIME_T&gt;
     * </PRE>
     */
    public void visit(LastUpdatedPart n) {
        n.nodeToken.accept(this);
    }

    /**
     * <PRE>
     * nodeOptional -> [ &lt;UNITS_T&gt; &lt;TEXT_T&gt; ]
     * </PRE>
     */
    public void visit(UnitsPart n) {
        n.nodeOptional.accept(this);
    }

    /**
     * <PRE>
     * -> &lt;PRODUCT_RELEASE_T&gt;
     * nodeToken1 -> &lt;TEXT_T&gt;
     * </PRE>
     */
    public void visit(ProductReleasePart n) {
        n.nodeToken.accept(this);
    }

    /**
     * <PRE>
     * -> &lt;ENTERPRISE_T&gt;
     * identifier -> ValueIdentifier()
     * </PRE>
     */
    public void visit(EnterprisePart n) {
        if (! current.exists(n.identifier)) {
            Message.warning(n.identifier,
                        "not defined or imported in this module");
        }
    }

    /**
     * Grammar production:
     * <PRE>
     * identifier -> ValueIdentifier()
     * type -> Types()
     * </PRE>
     */
    public void visit(ValueType n) {
        if (! current.exists(n.identifier)) {
            Message.error(n.identifier,
                                "not defined or imported in this module");
        }
        if (n.type.restrictionPresent()) {
            Message.error(n.type,
                                "sub typing is not allowed in a SEQUENCE definition");
        }
    }

    private void checkAssignedValue(Identifier id, AssignedValue val) 
                                     throws SemanticsException {
        int val_size = val.oidValue.size();
        /* Has the oid value hierarchy?? */
        if (val_size < 2) {
            Message.error(id, "has no parent defined (need 2 oids at least)");
            return;
        }
        /* Is the first oid value in the numerical range?? */
        OidValue parent = (OidValue)val.oidValue.firstElement();
        if (3 <= ((NumericValue)parent.getNumber()).getValue()) {
           Message.error(id, "parent number " + parent.getNumber() + " not accepted");
        }
        /* Has the last oid value the same name as assignment when present?? */
        /* Otherwise add the name. */
        OidValue child = (OidValue)val.oidValue.lastElement();
        if (child.hasName()) {
            if (! id.equals(child.getName())) {
                throw new SemanticsException("different name as the last Oid");
            }
        } else {
            child.setName(id);
        }

        if (parent.hasName()) {
            /* Check if all (except first and last) values have a name. */
            for (int i = 0; i < val_size - 2 ; i++) {
                parent = (OidValue)val.oidValue.elementAt(i);
                child = (OidValue)val.oidValue.elementAt(i+1);
                if (child.hasName() && ! child.hasNumber()) {
                    throw new SemanticsException("name only is not allowed");
                }
                if (! child.hasName()) {
                    String newname = new String(parent.getName() + "." + child.getNumber().getValue());
                    ((OidValue)val.oidValue.elementAt(i+1)).setName(newname);
                }
            }
        }
    }

    private void checkVariablesDefined(NodeList list) {
        checkVariablesDefined(list, current,
                                "not defined or imported in this module");
    }
    private void checkVariablesDefined(NodeList list, ModuleIdentifier mod_id) {
        try {
            SymbolHashTable table = ModuleHashTable.lookup(mod_id);
            checkVariablesDefined(list, table,
                                "not defined in module '" + mod_id + "'");
        } catch (SmiException exception) {
            Message.error(mod_id, "module is not loaded");
        }
    }
    private void checkVariablesDefined(NodeList list, SymbolHashTable table,
                                        String msg) {
        Enumeration e = list.elements();   
        while (e.hasMoreElements()) {
                Identifier id = (Identifier)e.nextElement();
                if (! table.exists(id)) {
                    Message.warning(id, msg);
                }
        }
    }

}
