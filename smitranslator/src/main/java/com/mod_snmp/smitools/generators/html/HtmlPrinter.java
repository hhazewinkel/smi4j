package com.mod_snmp.smitools.generators.html;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

import com.mod_snmp.smiparser.syntaxtree.*;
import com.mod_snmp.smiparser.visitor.DepthFirstVisitor;
import com.mod_snmp.smiparser.visitor.Visitor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Enumeration;

/**
 * Provides the method for generating a HTML version of the MIB module.
 */
public class HtmlPrinter extends DepthFirstVisitor implements Visitor {
    /* The stream to which the HTML is directed */
    HtmlWriter out;

    public HtmlPrinter(ModuleDefinition md) {
        this(System.out, md);
    }
    public HtmlPrinter(OutputStream o, ModuleDefinition md) {
        out = new HtmlWriter(o);
        md.accept(this);
    }
    public HtmlPrinter(String dirName, ModuleDefinition md)
                               throws FileNotFoundException {
        FileOutputStream fo = new FileOutputStream(
                                dirName + "/" + md.moduleIdentifier.toString());
        out = new HtmlWriter(fo);
        md.accept(this);
    }

    public void visit(NodeToken n) {
        out.print(n.toString());
    }

    /**
     * <PRE>
     * moduleIdentifier -> ModuleIdentifier()
     * -> &lt;DEFINITIONS_T&gt;
     * -> &lt;ASSIGN_T&gt;
     * -> &lt;BEGIN_T&gt;
     * exports_list -> [ &lt;EXPORTS_T&gt; ModuleExport() &lt;SEMI_COLON_T&gt; ]
     * import_module_list -> [ &lt;IMPORTS_T&gt; ( ModuleImport() )* &lt;SEMI_COLON_T&gt; ]
     * assignmentList -> ( Assignment() )*
     *  -> &lt;END_T&gt;
     * </PRE>
     */
    public void visit(ModuleDefinition n) {
        out.println("<HTML>");
        out.println("<HEAD>");
        out.println("</HEAD>");
        out.println("<BODY>");
        out.printSectionBegin();
        n.moduleIdentifier.accept(this);
        out.printKeyword("DEFINITIONS");
        out.printKeyword("::=");
        out.printKeyword("BEGIN");
        n.exports_list.accept(this);
        n.import_module_list.accept(this);
        n.assignmentList.accept(this);
        out.printKeyword("END");
        out.printSectionEnd();
        out.println("</BODY>");
        out.flush();
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
        out.printSectionBegin();
        Enumeration e = n.symbolList.elements();
        if (e.hasMoreElements()) {
            out.printSymbol(n.moduleIdentifier, 
                            ((Node)e.nextElement()).toString());
        }
        while (e.hasMoreElements()) {
            out.printComma();
            out.printSymbol(n.moduleIdentifier, 
                            ((Node)e.nextElement()).toString());
        }
        out.printBreak();
        out.printKeyword("FROM");
        n.moduleIdentifier.accept(this);
        out.printSectionEnd();
    }

   /**
     * <PRE>
     * nodeChoice -> TypeIdentifier() &lt;ASSIGN_T&gt; Types()
     * </PRE>
     */      
    public void visit(AssignmentType n) {
        out.printSectionBegin();
        out.printIdentifier(n.identifier);
        out.printKeyword("::=");
        n.type.accept(this);
        out.printSectionEnd();
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
        out.printKeyword("SEQUENCE");
        out.printLBrace();
        n.vtList.accept(this);
        out.printRBrace();
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
        out.printKeyword("TEXTUAL-CONVENTION");
        out.printIndent(+1);
        n.displayhint.accept(this);
        n.status.accept(this);
        n.description.accept(this);
        n.reference.accept(this);
        n.syntax.accept(this);
        out.printIndent(-1);
    }

    /**
     * <PRE>
     * ->  &lt;CHOICE_T&gt;
     * choice ->  ChoiceValue()
     * </PRE>
     */
    public void visit(TypeChoice n) {
        out.printKeyword("CHOICE");
        n.vtList.accept(this);
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
        if (n.present()) {
            out.printLSquare();
            n.sort.accept(this);
            n.number.accept(this);
            out.printRSquare();
            n.kind.accept(this);
        }
        n.type.accept(this);
    }           

    /** 
     * <PRE>
     * identifier -> TypeIdentifier()
     * restriction -> [ Restriction() ]
     * </PRE>
     */
    public void visit(TypeIdentifier n) {
        n.identifier.accept(this);
        n.restriction.accept(this);
    }       

    /**
     * <PRE> 
     * -> &lt;SEQUENCE_OF_T&gt;
     * identifier -> TypeIdentifier()
     * </PRE>
     */
    public void visit(TypeSequenceOf n) {
        out.printKeyword("SEQUENCE OF");
        n.identifier.accept(this);
    }

    /**
     * <PRE>
     * identifier -> PredefinedMacros()  smi_defined == true
     *               |
     *               TypeIdentifier()    smi_defined == false
     * -> &lt;MACRO_T&gt;
     * -> &lt;ASSIGN_T&gt;
     * -> &lt;BEGIN_T&gt;
     * macro -> MacroBody()      
     * -> &lt;END_T&gt;
     * </PRE>
     */
    public void visit(AssignmentMacro n) {
        out.printSectionBegin();
        out.printIdentifier(n.identifier);
        out.printKeyword("MACRO");
        out.printKeyword("::=");
        out.printKeyword("BEGIN");
        n.macro.accept(this);
        out.printKeyword("END");
        out.printSectionEnd();
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
        out.printSectionBegin();
        out.printIdentifier(n.identifier);
        n.info.accept(this);
        out.printIndent(+1);
        out.printKeyword("::=");
        n.assignedValue.accept(this);
        out.printIndent(-1);
        out.printSectionEnd();
    }

    /**
     * <PRE>
     * identifier -> ValueIdentifier()
     * -> &lt;OBJECT_IDENTIFIER_T&gt;
     * -> &lt;ASSIGN_T&gt;
     * assignedValue -> AssignedValue()
     * </PRE>
     */
    public void visit(ObjectInfoObjectIdentifier n) {
        out.printKeyword("OBJECT IDENTIFIER");
    }

    /**
     * <PRE>
     * -> Arbitrary not parsed stuff
     * </PRE>
     */
    public void visit(MacroBody n) {
        out.printSectionBegin();
        out.printSectionEnd();
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
        out.printKeyword("OBJECT-TYPE");
        out.printIndent(+1);
        n.syntaxPart.accept(this);
        n.unitsPart.accept(this);
        n.max_access.accept(this);
        n.statusPart.accept(this);
        n.descriptionPart.accept(this);
        n.referencePart.accept(this);
        n.indexPart.accept(this);
        n.defValPart.accept(this);
        out.printIndent(-1);
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
        out.printKeyword("MODULE-IDENTITY");
        out.printIndent(+1);
        n.lastUpdatedPart.accept(this);
        n.organizationPart.accept(this);
        n.contactInfoPart.accept(this);
        n.descriptionPart.accept(this);
        n.revisions.accept(this);
        out.printIndent(-1);
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
        out.printKeyword("OBJECT-IDENTITY");
        out.printIndent(+1);
        n.statusPart.accept(this);
        n.descriptionPart.accept(this);
        n.referencePart.accept(this);
        out.printIndent(-1);
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
        out.printKeyword("OBJECT-GROUP");
        out.printIndent(+1);
        if (n.objectsPart.present()) {
            out.printSectionBegin();
            out.printKeyword("OBJECTS");
            out.printLBrace();
            out.printIdentifierList(n.objectsPart);
            out.printRBrace();
            out.printSectionEnd();
        }
        n.statusPart.accept(this);
        n.descriptionPart.accept(this);
        n.referencePart.accept(this);
        out.printIndent(-1);
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
        out.printKeyword("NOTIFICATION-TYPE");
        out.printIndent(+1);
        if (n.objectsPart.present()) {
            out.printSectionBegin();
            out.printKeyword("OBJECTS");
            out.printLBrace();
            out.printIdentifierList(n.objectsPart);     
            out.printRBrace();
            out.printSectionEnd();
        }
        n.statusPart.accept(this);
        n.descriptionPart.accept(this);
        n.referencePart.accept(this);
        out.printIndent(-1);
    }

    /**
     * <PRE>
     * -> &lt;NOTIFICATION_GROUP_T&gt;
     * notifications -> NotificationPart()
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
        out.printKeyword("NOTIFICATION-GROUP");
        out.printIndent(+1);
        /* NotificationPart */
        out.printSectionBegin();
        out.printKeyword("NOTIFICATIONS");
        out.printLBrace();
        n.notifications.accept(this);
        out.printRBrace();
        out.printSectionEnd();
        /* StatusPart */
        n.statusPart.accept(this);
        n.descriptionPart.accept(this);
        n.referencePart.accept(this);
        out.printIndent(-1);
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
        out.printKeyword("MODULE-COMPLIANCE");
        out.printIndent(+1);
        n.statusPart.accept(this);
        n.descriptionPart.accept(this);
        n.referencePart.accept(this);
        n.moduleCompliancePart.accept(this);
        out.printIndent(-1);
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
        out.printKeyword("AGENT-CAPABILITIES");
        out.printIndent(+1);
        n.productReleasePart.accept(this);
        n.statusPart.accept(this);
        n.descriptionPart.accept(this);
        n.referencePart.accept(this);
        n.moduleCapabilitiesPart.accept(this);
        out.printIndent(-1);
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
        out.printSectionBegin();
        out.printKeyword("TRAP-TYPE");
        out.printIndent(+1);
        n.enterprisePart.accept(this);
        if (n.variablesPart.present()) {
            out.printSectionBegin();   
            out.printKeyword("VARIABLES");
            out.printLParen();
            out.printIdentifierList(n.variablesPart);
            out.printRParen();
            out.printSectionEnd();
        }
        n.descriptionPart.accept(this);
        n.number.accept(this);
        out.printIndent(-1);
        out.printSectionEnd();
    }

    /**
     * Grammar production:
     * <PRE>
     * nodeList -> &lt;REVISION_T&gt; &lt;UTC_TIME_T&gt; DescriptionPart()     
     * </PRE>
     * Syntaxtree :
     * <PRE>
     * time -> &lt;UTC_TIME_T&gt;
     * descr -> DescriptionPart()   
     * </PRE>
     */
    public void visit(Revision n) {
        out.printKeyword("REVISION");
        n.time.accept(this);
        n.descr.accept(this);
    }

    /**
     * <PRE>
     * nodeToken -> &lt;STATUS_T&gt;
     * nodeChoice -> ( &lt;CURRENT_T&gt; | &lt;DEPRECATED_T&gt; | &lt;OBSOLETE_T&gt; | &lt;MANDATORY_T&gt; )
     * </PRE>
     */
    public void visit(StatusPart n) {
        out.printSectionBegin();
        out.printKeyword("STATUS");
        n.value.accept(this);
        out.printSectionEnd();
    }

    /**
     * <PRE>
     * nodeChoice -> ( &lt;ACCESS_T&gt; | &lt;MAX_ACCESS_T&gt; | &lt;MIN_ACCESS_T&gt; )
     * nodeChoice1 -> ( &lt;NOT_ACCESSIBLE_T&gt; | &lt;ACCESSIBLE_FOR_NOTIFY_T&gt; | &lt;READ_ONLY_T&gt; | &lt;READ_WRITE_T&gt; | &lt;READ_CREATE_T&gt; | &lt;WRITE_ONLY_T&gt; | &lt;NOT_IMPLEMENTED_T&gt; )
     * </PRE>
     */
    public void visit(AccessPart n) {
        out.printSectionBegin();
        out.printKeyword("ACCESS");
        n.value.accept(this);
        out.printSectionEnd();
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
        n.module_id.accept(this);
        if (n.mandatory_part.present()) {
            out.printSectionBegin();
            out.printKeyword("MANDATORY-GROUPS");
            out.printLBrace();
            n.mandatory_part.accept(this);
            out.printRBrace();
            out.printSectionEnd();
        }
        n.compliance_part.accept(this);
    }

    /** 
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
        out.printSectionBegin(); 
        out.printKeyword("SUPPORTS");
        n.module_id.accept(this);
        out.printKeyword("INCLUDES");
        out.printLBrace();
        n.object_list.accept(this);
        out.printRBrace();
        n.variation_part.accept(this);
        out.printSectionEnd();
    }

    /**
     * <PRE>
     * nodeToken -> &lt;VARIATION_T&gt;
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
        out.printSectionBegin();
        out.printKeyword("VARIATION");
        out.printIdentifier(n.identifier);
        n.syntaxPart.accept(this);
        n.write_syntax.accept(this);
        n.access.accept(this);
        n.creation_requires.accept(this);
        n.defValPart.accept(this);
        n.descriptionPart.accept(this);
        out.printSectionEnd();
    }

    /**
     * <PRE>
     * nodeToken -> &lt;SYNTAX_T&gt;
     * type -> Types()
     * </PRE>
     */
    public void visit(SyntaxPart n) {
        out.printSectionBegin();
        out.printKeyword("SYNTAX");
        n.type.accept(this);
        out.printSectionEnd();
    }
        
    /** 
     * <PRE>
     * nodeToken -> &lt;WRITE-SYNTAX_T&gt;
     * type -> Types()
     * </PRE>
     */
    public void visit(WriteSyntaxPart n) {
        out.printSectionBegin();
        out.printKeyword("WRITE-SYNTAX");
        n.type.accept(this);
        out.printSectionEnd();
    }

    /**
     * <PRE>
     * -> &lt;GROUP_T&gt; ValueIdentifier() DescriptionPart()
     * identifier -> ValueIdentifier()
     * description -> DescriptionPart()
     * </PRE>
     */
    public void visit(ComplianceGroup n) {
        out.printKeyword("GROUP");
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
        out.printKeyword("OBJECT");
        n.identifier.accept(this);
        n.syntax.accept(this);
        n.write_syntax.accept(this);
        n.min_access.accept(this);
        n.description.accept(this);
    }

    /**
     * <PRE>
     * -> ValueIdentifier() ( &lt;COMMA_T&gt; ValueIdentifier() )*
     * list -> ( ValueIdentifier() )*
     * </PRE>
     */
    public void printVariableList(NodeList list) {
        Enumeration e = list.elements();
        if (e.hasMoreElements()) {
            out.printIdentifier((Identifier)e.nextElement());
        }
        while (e.hasMoreElements()) {
            out.printComma();
            out.printIdentifier((Identifier)e.nextElement());
        }
    }

    /**
     * <PRE>
     * nodeChoice -> &lt;SEQUENCE_T&gt; &lt;LEFT_BRACE_T&gt; ValueIdentifier() Types() ( &lt;COMMA_T&gt; ValueIdentifier() Types() )* &lt;RIGHT_BRACE_T&gt;
     *       | &lt;SEQUENCE_T&gt; TypeIdentifier()
     *       | &lt;SEQUENCE_OF_T&gt; TypeIdentifier()
     *       | &lt;INTEGER_T&gt; [ RestrictionInteger() ]
     *       | &lt;INTEGER32_T&gt; [ RestrictionInteger() ]
     *       | &lt;UINTEGER32_T&gt; [ RestrictionInteger() ]
     *       | &lt;UNSIGNED32_T&gt; [ RestrictionInteger() ]
     *       | &lt;BITS_T&gt; [ RestrictionNamedNumberList() ]
     *       | &lt;BIT_STRING_T&gt; [ RestrictionNamedNumberList() ]
     *       | &lt;OCTET_STRING_T&gt; [ RestrictionSize() ]
     *       | &lt;OBJECT_IDENTIFIER_T&gt;
     *       | &lt;COUNTER_T&gt;
     *       | &lt;COUNTER32_T&gt;
     *       | &lt;COUNTER64_T&gt;
     *       | &lt;GAUGE_T&gt; [ RestrictionInteger() ]
     *       | &lt;GAUGE32_T&gt; [ RestrictionInteger() ]
     *       | &lt;TIMETICKS_T&gt;
     *       | &lt;OPAQUE_T&gt; [ RestrictionSize() ]
     *       | &lt;NSAP_ADDRESS_T&gt;
     *       | &lt;IP_ADDRESS_T&gt;
     *       | &lt;NETWORK_ADDRESS_T&gt;
     *       | TypeIdentifier() [ Restriction() ]
     * </PRE>
     */
    public void visit(TypeSmi n) {
        n.base.accept(this);
        n.restriction.accept(this);
    }

    /**
     * <PRE>
     * nodeToken -> &lt;LEFT_PAREN_T&gt;
     * range -> Range()
     * nodeToken1 -> &lt;RIGHT_PAREN_T&gt;
     * </PRE>
     */
    public void visit(RestrictionRange n) {
        out.printLParen();
        n.range.accept(this);
        out.printRParen();
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
        out.printLParen();
        out.printKeyword("SIZE");
        out.printLParen();
        n.range.accept(this);
        out.printRParen();
        out.printRParen();
    }

    /**
     * <PRE>
     * nodeToken -> &lt;LEFT_BRACE_T&gt;
     * -> NamedNumber() ( &lt;COMMA_T&gt; NamedNumber() )*
     * list -> NamedNumber() ( NamedNumber() )*
     * nodeToken1 -> &lt;RIGHT_BRACE_T&gt;
     * </PRE>
     */
    public void visit(RestrictionNamedNumberList n) {
        out.printSectionBegin();
        out.printLBrace();
        Enumeration e = n.list.elements();
        while (e.hasMoreElements()) {
            ((NamedNumber)e.nextElement()).accept(this);
            out.printSpace();
        }
        out.printRBrace();
        out.printSectionEnd();
    }

    /**
     * <PRE>
     * list-> RangeItem() ( &lt;OR_T&gt; RangeItem() )*
     * </PRE>
     */
    public void visit(RangeList n) {
        out.printSectionBegin();
        Enumeration e = n.list.elements();
        while (e.hasMoreElements()) { 
            ((RangeItem)e.nextElement()).accept(this);
            out.printSpace();
        }
        out.printSectionEnd();
    }
        
    /**     
     * <PRE>
     * -> NumericValue()
     *    [ &lt;UNTIL_T&gt; NumericValue() ]
     * begin -> NumericValue()  
     * end -> NumericValue() -- NodeOptional --
     * </PRE>
     */  
    public void visit(RangeItem n) {
        n.begin.accept(this);
        if (n.end.present()) {
            out.printUntil();
            n.end.accept(this);
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
        out.printIdentifier(n.name);
        out.printLParen();
        n.number.accept(this);
        out.printRParen();
    }

    /**
     * <PRE>
     * nodeToken -> &lt;TYPE_IDENTIFIER_T&gt;
     * </PRE>
     */
    public void visit(ModuleIdentifier n) {
        out.printModuleIdentifier(n);
    }

    /**
     * <PRE>
     *-> [ ( &lt;INDEX_T&gt; &lt;LEFT_BRACE_T&gt; IndexTypes() &lt;RIGHT_BRACE_T&gt; ]
     * list -> Index () ( &lt;COMMA_T&gt; Index() )*
     * </PRE>
     */
    public void visit(IndexList n) {
        out.printSectionBegin();
        n.list.accept(this);
        out.printSectionEnd();
    }

    /**
     * <PRE>
     * nodeToken -> &lt;LEFT_BRACE_T&gt;
     * oidValue -> OidValue() ( OidValue() )+
     * nodeToken1 -> &lt;RIGHT_BRACE_T&gt;
     * </PRE>
     */
    public void visit(AssignedValue n) {
        out.printLBrace();
        Enumeration e = n.oidValue.nodes.elements();
        while (e.hasMoreElements()) {
            ((OidValue)e.nextElement()).accept(this);
        }
        out.printRBrace();
    }

    /**
     * <PRE>
     * nodeChoice -> ValueIdentifier() [ &lt;LEFT_PAREN_T&gt; NumericValue() &lt;RIGHT_PAREN_T&gt; ]
     *       | NumericValue()
     * </PRE>
     */
    public void visit(OidValue n) {
        if (n.isGrammarSet()) {
            out.printIdentifier(n.name);
            if (n.hasNumber()) {
                out.printLParen();
                n.numval.accept(this);
                out.printRParen();
            }
        } else if (n.hasNumber()) {
            n.numval.accept(this);
        }
        out.print(" ");
    }

    /**
     * <PRE>
     * nodeChoice -> &lt;DEC_NUMBER_T&gt;
     *       | &lt;BIN_NUMBER_T&gt;
     *       | &lt;HEX_NUMBER_T&gt;
     * </PRE>
     */
    public void visit(NumericValue n) {
        out.printNumber(n);
    }

    /**
     * <PRE>
     * nodeToken -> &lt;DESCRIPTION_T&gt;
     * nodeToken1 -> &lt;TEXT_T&gt;
     * </PRE>
     */
    public void visit(DescriptionPart n) {
        out.printSectionBegin();
        out.printKeyword("DESCRIPTION");
        out.printText(n.descr);
        out.printSectionEnd();
    }

    /**
     * <PRE>
     * nodeOptional -> [ &lt;REFERENCE_T&gt; &lt;TEXT_T&gt; ]
     * </PRE>
     */
    public void visit(ReferencePart n) {
        out.printSectionBegin();
        n.nodeOptional.accept(this);
        //out.printText();
        out.printSectionEnd();
    }

    /**
     * <PRE>
     * nodeToken -> &lt;CONTACT_INFO_T&gt;
     * nodeToken1 -> &lt;TEXT_T&gt;
     * </PRE>
     */
    public void visit(ContactInfoPart n) {
        out.printSectionBegin();
        out.printKeyword("CONTACT-INFO");
        out.printText(n.nodeToken);
        out.printSectionEnd();
    }

    /**
     * <PRE>
     * nodeToken -> &lt;ORGANIZATION_T&gt;
     * nodeToken1 -> &lt;TEXT_T&gt;
     * </PRE>
     */
    public void visit(OrganizationPart n) {
        out.printSectionBegin();
        out.printKeyword("ORGANIZATION");
        out.printText(n.nodeToken);
        out.printSectionEnd();
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
        out.printSectionBegin();
        out.printKeyword("DEFVAL");
        out.printLBrace();
        n.nodeOptional.accept(this);
        out.printRBrace();
        out.printSectionEnd();
    }

    /**
     * <PRE>
     * list -> ( ValueIdentifier() )*
     * </PRE>
     */
    public void visit(BitValueList n) {
        out.printSectionBegin();
        n.list.accept(this);
        out.printSectionEnd();
    }

    /**
     * <PRE>
     * nodeOptional -> [ &lt;DISPLAY_HINT_T&gt; &lt;TEXT_T&gt; ]
     * </PRE>
     */
    public void visit(DisplayHintPart n) {
        if (n.present()) {
            out.printSectionBegin();
            out.printKeyword("DISPLAY-HINT");
            n.display_hint.accept(this);
            out.printSectionEnd();
        }
    }

    /**
     * <PRE>
     * nodeToken -> &lt;LAST_UPDATED_T&gt;
     * nodeToken1 -> &lt;UTC_TIME_T&gt;
     * </PRE>
     */
    public void visit(LastUpdatedPart n) {
        out.printSectionBegin();
        out.printKeyword("LAST-UPDATED");
        n.nodeToken.accept(this);
        out.printSectionEnd();
    }

    /**
     * <PRE>
     * nodeOptional -> [ &lt;UNITS_T&gt; &lt;TEXT_T&gt; ]
     * </PRE>
     */
    public void visit(UnitsPart n) {
        out.printSectionBegin();
        n.nodeOptional.accept(this);
        out.printSectionEnd();
    }

    /**
     * <PRE>
     * nodeToken -> &lt;PRODUCT_RELEASE_T&gt;
     * nodeToken1 -> &lt;TEXT_T&gt;
     * </PRE>
     */
    public void visit(ProductReleasePart n) {
        out.printSectionBegin();
        out.printKeyword("PRODUCT-RELEASE");
        out.printText(n.nodeToken);
        out.printSectionEnd();
    }

    /**
     * <PRE>
     * nodeToken -> &lt;ENTERPRISE_T&gt;
     * identifier -> ValueIdentifier()
     * </PRE>
     */
    public void visit(EnterprisePart n) {
        out.printSectionBegin();
        out.printKeyword("ENTERPRISE");
        out.printIdentifier(n.identifier);
        out.printSectionEnd();
    }

    /**
     * Grammar production:
     * <PRE>
     * identifier -> ValueIdentifier()
     * type -> Types() 
     * </PRE>
     */
    public void visit(ValueType n) {
        out.printIdentifier(n.identifier); 
        n.type.accept(this);
    }

}
