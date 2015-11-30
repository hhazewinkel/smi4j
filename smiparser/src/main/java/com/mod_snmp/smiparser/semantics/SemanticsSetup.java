package com.mod_snmp.smiparser.semantics;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/
/**
 * SemanticsSetup
 * Class that walks thru the syntaxtree to setup semantical checking.
 */

import com.mod_snmp.smiparser.errorhandler.ErrorException;
import com.mod_snmp.smiparser.errorhandler.Message;
import com.mod_snmp.smiparser.errorhandler.SmiException;
import com.mod_snmp.smiparser.syntaxtree.*;
import com.mod_snmp.smiparser.visitor.DepthFirstVisitor;
import com.mod_snmp.smiparser.visitor.Visitor;

import java.util.Enumeration;

public class SemanticsSetup extends DepthFirstVisitor implements Visitor {
    private static SymbolHashTable current;

    /**
     * <PRE>
     * moduleIdentifier -> ModuleIdentifier()
     * -> &lt;DEFINITIONS_T&gt;
     * -> &lt;ASSIGN_T&gt;
     * -> &lt;BEGIN_T&gt;
     * exports_list -> [ &lt;EXPORTS_T&gt;
     *                   ModuleExport()
     *                   &lt;SEMI_COLON_T&gt; ]
     * import_module_list -> [ &lt;IMPORTS_T&gt;
     *                         ( ModuleImport() )*
     *                         &lt;SEMI_COLON_T&gt; ]
     * assignmentList -> ( Assignment() )*
     * -> &lt;END_T&gt;
     * </PRE>
     * Create for each ModuleDefinition a symbol hash table
     * and store the table in a global module hash table.
     * It is not possible to create symbol hash tables that
     * have equal module names.
     */
    public void visit(ModuleDefinition n) {
        try {
            current = new SymbolHashTable();
            current.insert(n.moduleIdentifier, n);
            ModuleHashTable.insert(n.moduleIdentifier, current);
        } catch (SmiException exception) {
            Message.error(n.moduleIdentifier, exception.getMessage());
        }
        n.import_module_list.accept(this);
        n.assignmentList.accept(this);
    }

    /**
     * <PRE>
     * PredefinedMacro() ->
     *         ( &lt;OBJECT_TYPE_T&gt;        | &lt;OBJECT_IDENTITY_T&gt;
     *         | &lt;OBJECT_GROUP_T&gt;       | &lt;TEXTUAL_CONVENTION_T&gt;
     *         | &lt;NOTIFICATION_TYPE_T&gt;  | &lt;NOTIFICATION_GROUP_T&gt;
     *         | &lt;MODULE_IDENTITY_T&gt;    | &lt;MODULE_COMPLIANCE_T&gt;
     *         | &lt;AGENT_CAPABILITIES_T&gt; | &lt;TRAP_TYPE_T&gt; )
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
        Enumeration e = n.symbolList.elements();
        while (e.hasMoreElements()) {
            Identifier id = (Identifier)e.nextElement();
            try {
                current.insert(id, n.moduleIdentifier);
            } catch (ErrorException exception) {
                Message.error(id, "imported twice");
            }
        }
    }

   /**
     * <PRE>
     * identifier -> TypeIdentifier()
     * type -> Types()
     * </PRE>
     * Insert the type in the symbol hash table by the identifier.
     */      
    public void visit(AssignmentType n) {
        try {
            current.insert(n.identifier, n.type);
        } catch (ErrorException exception) {
            Message.error(n.identifier, "type assigned a type twice");
        }
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
        //n.vtList.accept(this);
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
        //n.displayhint.accept(this);
        //n.status.accept(this);
        //n.description.accept(this);
        //n.reference.accept(this);
        //n.syntax.accept(this);
    }

    /**
     * <PRE>
     * ->  &lt;CHOICE_T&gt;
     * choice ->  TypeChoice()
     * </PRE>
     */
    public void visit(TypeChoice n) {
        //n.choice.accept(this);
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

     * smiTypes -> SmiTypes()
     * -> &lt;ASSIGN_T&gt;
     * tag -> [ Tag() ]
     * type -> Types()
     * </PRE>   
     */     
    public void visit(TypeTag n) { 
        if (n.present()) {
            //n.sort.accept(this);
            //n.number.accept(this);
            //n.kind.accept(this);
        }
        //n.types.accept(this);
    }           

    /**
     * <PRE>
     * identifier -> PredefinedMacros()
     * -> &lt;MACRO_T&gt;
     * -> &lt;ASSIGN_T&gt;
     * -> &lt;BEGIN_T&gt;
     * macro -> MacroBody()      
     * -> &lt;END_T&gt;
     * </PRE>
     */
    public void visit(AssignmentMacro n) {
        try {
            current.insert(n.identifier, n);
        } catch (ErrorException exception) {
            Message.error(n.identifier, "MACRO assigned a type twice");
        }
    }

    /**
     * <PRE>
     * identifier -> ValueIdentifier()
     * info-> ObjectInfo()
     * -> &lt;ASSIGN_T&gt;
     * assignedValue -> AssignedValue()
     * </PRE>
     * Insert the assignment in the symbol hash table by the identifier.
     */
    public void visit(AssignmentObject n) {
        try {
            current.insert(n.identifier, n);
        } catch (ErrorException exception) {
            Message.error(n.identifier, "object assigned a value twice");
        }
    }

    /**
     * <PRE>
     * -> &lt;OBJECT_IDENTIFIER_T&gt;
     * </PRE>
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
     * accessPart -> AccessPart()
     * statusPart -> StatusPart()
     * descriptionPart -> DescriptionPart()
     * referencePart -> ReferencePart()
     * indexPart -> IndexPart()
     * defValPart -> DefValPart()
     * </PRE>
     */
    public void visit(ObjectInfoObjectType n) {
        //n.syntaxPart.accept(this);
        //n.unitsPart.accept(this);
        //n.accessPart.accept(this);
        //n.statusPart.accept(this);
        //n.descriptionPart.accept(this);
        //n.referencePart.accept(this);
        //n.indexPart.accept(this);
        //n.defValPart.accept(this);
    }

    /**
     * <PRE>
     * -> &lt;MODULE_IDENTITY_T&gt;
     * lastUpdatedPart -> LastUpdatedPart()
     * organizationPart -> OrganizationPart()
     * contactInfoPart -> ContactInfoPart()
     * descriptionPart -> DescriptionPart()
     * revisionPart -> RevisionPart()
     * </PRE>
     */
    public void visit(ObjectInfoModuleIdentity n) {
        //n.lastUpdatedPart.accept(this);
        //n.organizationPart.accept(this);
        //n.contactInfoPart.accept(this);
        //n.descriptionPart.accept(this);
        //n.revisionPart.accept(this);
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
        //n.statusPart.accept(this);
        //n.descriptionPart.accept(this);
        //n.referencePart.accept(this);
        //n.assignedValue.accept(this);
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
        //n.objectsPart.accept(this);
        //n.statusPart.accept(this);
        //n.descriptionPart.accept(this);
        //n.referencePart.accept(this);
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
        //n.objectsPart.accept(this);
        //n.statusPart.accept(this);
        //n.descriptionPart.accept(this);
        //n.referencePart.accept(this);
    }

    /**
     * <PRE>
     * -> &lt;NOTIFICATION_GROUP_T&gt;
     * notificationPart -> NotificationPart()
     * statusPart -> StatusPart()
     * descriptionPart -> DescriptionPart()
     * referencePart -> ReferencePart()
     * </PRE>
     */
    public void visit(ObjectInfoNotificationGroup n) {
        //n.notificationPart.accept(this);
        //n.statusPart.accept(this);
        //n.descriptionPart.accept(this);
        //n.referencePart.accept(this);
    }

    /**
     * <PRE>
     * -> &lt;MODULE_COMPLIANCE_T&gt;
     * statusPart -> StatusPart()
     * descriptionPart -> DescriptionPart()
     * referencePart -> ReferencePart()
     * moduleCompliancePart -> ModuleCompliancePart()
     * </PRE>
     */
    public void visit(ObjectInfoModuleCompliance n) {
        //n.statusPart.accept(this);
        //n.descriptionPart.accept(this);
        //n.referencePart.accept(this);
        //n.moduleCompliancePart.accept(this);
    }

    /**
     * <PRE>
     * -> &lt;AGENT_CAPABILITIES_T&gt;
     * productReleasePart -> ProductReleasePart()
     * statusPart -> StatusPart()
     * descriptionPart -> DescriptionPart()
     * referencePart -> ReferencePart()
     * moduleCapabilitiesPart -> ModuleCapabilitiesPart()
     * </PRE>
     */
    public void visit(ObjectInfoAgentCapabilities n) {
        //n.productReleasePart.accept(this);
        //n.statusPart.accept(this);
        //n.descriptionPart.accept(this);
        //n.referencePart.accept(this);
        //n.moduleCapabilitiesPart.accept(this);
        //n.assignedValue.accept(this);
    }

    /**
     * <PRE>
     * identifier -> ValueIdentifier()
     * -> &lt;TRAP_TYPE_T&gt;
     * enterprisePart -> EnterprisePart()
     * variablesPart -> VariablesPart()
     * descriptionPart -> DescriptionPart()
     * -> &lt;ASSIGN_T&gt;
     * number -> NumericValue()
     * </PRE>
     */
    public void visit(ObjectInfoTrapType n) {
        //n.enterprisePart.accept(this);
        //n.variablesPart.accept(this);
        //n.descriptionPart.accept(this);
        //n.number.accept(this);
    }

}
