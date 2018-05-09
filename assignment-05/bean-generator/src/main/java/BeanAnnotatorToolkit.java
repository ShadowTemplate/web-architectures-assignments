import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BeanAnnotatorToolkit {

    public static String annotateUnit(String classFile, boolean hasEmptyConstructor) throws ParseException, IOException {
        try (FileInputStream in = new FileInputStream(classFile)) {
            CompilationUnit classSourceCode = JavaParser.parse(in);
            BeanAnnotatorVisitor annotator = new BeanAnnotatorVisitor(hasEmptyConstructor);
            annotator.visit(classSourceCode, null);
            return classSourceCode.toString();
        }
    }

    private static class BeanAnnotatorVisitor extends VoidVisitorAdapter {

        private boolean hasEmptyConstructor;

        BeanAnnotatorVisitor(boolean hasEmptyConstructor) {
            this.hasEmptyConstructor = hasEmptyConstructor;
        }

        @Override
        public void visit(CompilationUnit compilationUnit, Object arg) {
            super.visit(compilationUnit, arg);

            // add import statement: import javax.persistence.*;
            ImportDeclaration importDeclaration = new ImportDeclaration(new NameExpr("javax.persistence"), false, true);
            compilationUnit.getImports().add(importDeclaration);
        }

        @Override
        public void visit(ClassOrInterfaceDeclaration classDeclaration, Object arg) {
            super.visit(classDeclaration, arg);

            String className = classDeclaration.getName();
            List<AnnotationExpr> classAnnotations = classDeclaration.getAnnotations();
            List<BodyDeclaration> classMembers = classDeclaration.getMembers();

            // add annotation: @Entity
            classAnnotations.add(new MarkerAnnotationExpr(new NameExpr("Entity")));
            // add annotation: @Table(name="CLASSNAME")
            classAnnotations.add(buildValuedAnnotation("Table", "name", "\"" + className.toUpperCase() + "\""));

            // add empty constructor if needed
            if (!hasEmptyConstructor) {
                BodyDeclaration emptyConstructor = new ConstructorDeclaration(ModifierSet.PUBLIC,
                        new ArrayList<>(), new ArrayList<>(), "TestPojo", new ArrayList<>(),
                        new ArrayList<>(), new BlockStmt(new ArrayList<>()));
                classMembers.add(0, emptyConstructor);
            }

            // add an int field to be used as table identifier on the database
            // label the field with @Id, @Column(name="fieldName")
            // and @GeneratedValue(strategy = GenerationType.AUTO)
            String idFieldName = "tableIdFor" + className;

            List<AnnotationExpr> fieldAnnotations = new ArrayList<>();
            fieldAnnotations.add(new MarkerAnnotationExpr(new NameExpr("Id")));
            fieldAnnotations.add(buildValuedAnnotation("GeneratedValue", "strategy", "GenerationType.AUTO"));
            fieldAnnotations.add(buildValuedAnnotation("Column", "name", "\"" + idFieldName + "\""));

            FieldDeclaration idFieldDeclaration = new FieldDeclaration(ModifierSet.PRIVATE, fieldAnnotations,
                    new PrimitiveType(PrimitiveType.Primitive.Int),
                    Collections.singletonList(new VariableDeclarator(new VariableDeclaratorId(idFieldName))));

            // generate getter and setter for the newly added int field
            String idGetterName = "getTableIdFor" + className,
                    idSetterName = "setTableIdFor" + className;
            // generate getter: int getId() {}
            MethodDeclaration idFieldGetter = new MethodDeclaration(ModifierSet.PUBLIC,
                    new PrimitiveType(PrimitiveType.Primitive.Int), idGetterName);
            // generate the statement: return this.id;
            BlockStmt returnStatement = new BlockStmt(Collections.singletonList(new ReturnStmt(
                    new NameExpr("this." + idFieldName))));
            idFieldGetter.setBody(returnStatement);

            // generate setter: void setId(int id) {}
            MethodDeclaration idFieldSetter = new MethodDeclaration(ModifierSet.PUBLIC, new VoidType(), idSetterName,
                    Collections.singletonList(new Parameter(new PrimitiveType(PrimitiveType.Primitive.Int),
                            new VariableDeclaratorId(idFieldName))));
            // generate the statement: this.id = id;
            BlockStmt assignmentStatement = new BlockStmt(Collections.singletonList(new ExpressionStmt(
                    new NameExpr("this." + idFieldName + " = " + idFieldName))));
            idFieldSetter.setBody(assignmentStatement);

            classMembers.add(0, idFieldDeclaration);
            classMembers.add(idFieldGetter);
            classMembers.add(idFieldSetter);
        }

        @Override
        public void visit(FieldDeclaration fieldDeclaration, Object arg) {
            super.visit(fieldDeclaration, arg);

            // label each field with: @Column(name="fieldName")
            String fieldName = fieldDeclaration.getVariables().get(0).toString();
            AnnotationExpr columnAnnotation = buildValuedAnnotation("Column", "name", "\"" + fieldName + "\"");
            fieldDeclaration.getAnnotations().add(columnAnnotation);
        }

        private static SingleMemberAnnotationExpr buildValuedAnnotation(String annotation, String field, String value) {
            // return an annotation of the form: @Annotation(field=value)
            return new SingleMemberAnnotationExpr(new NameExpr(annotation), new AssignExpr(new NameExpr(field),
                    new NameExpr(value), AssignExpr.Operator.assign));
        }
    }
}