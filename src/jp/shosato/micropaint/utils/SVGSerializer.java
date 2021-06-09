package jp.shosato.micropaint.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.joml.Vector2d;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import jp.shosato.micropaint.components.BasicComponent;
import jp.shosato.micropaint.components.FigureComponent;
import jp.shosato.micropaint.components.figures.FreeLineFigure;
import jp.shosato.micropaint.components.figures.PolygonFigure;

public class SVGSerializer {
    Document doc;
    Transformer transf;

    public SVGSerializer(BasicComponent rootComponent) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.newDocument();

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transf = transformerFactory.newTransformer();

            transf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transf.setOutputProperty(OutputKeys.INDENT, "yes");
            transf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            Element root = doc.createElement("svg");
            root.setAttribute("xmlns", "http://www.w3.org/2000/svg");
            for (BasicComponent child : rootComponent.getChildren()) {
                if (child instanceof FigureComponent) {
                    root.appendChild(convertFigure((FigureComponent) child));
                }
            }
            doc.appendChild(root);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private Element convertFigure(FigureComponent figure) {
        Element elem;
        if (figure instanceof PolygonFigure) {
            elem = doc.createElement("polygon");
            StringBuilder builder = new StringBuilder();
            List<Vector2d> vertices = ((PolygonFigure) figure).getVertices();
            for (int i = 0, length = vertices.size(); i < length; i++) {
                Vector2d v = vertices.get(i);
                builder.append(String.format("%g %g", v.x, v.y));
                if (i != length - 1)
                    builder.append(", ");
            }
            elem.setAttribute("points", builder.toString());
        } else if (figure instanceof FreeLineFigure) {
            elem = doc.createElement("polyline");
            StringBuilder builder = new StringBuilder();
            List<Vector2d> vertices = ((FreeLineFigure) figure).getVertices();
            for (int i = 0, length = vertices.size(); i < length; i++) {
                Vector2d v = vertices.get(i);
                builder.append(String.format("%g %g", v.x, v.y));
                if (i != length - 1)
                    builder.append(", ");
            }
            elem.setAttribute("points", builder.toString());
        } else {
            System.err.println("Unsupported figure");
            return null;
        }
        elem.setAttribute("fill", Utility.getColorCode(figure.getFill()));
        elem.setAttribute("stroke", Utility.getColorCode(figure.getStroke()));
        elem.setAttribute("stroke-width", String.valueOf(figure.getStrokeWidth()));
        return elem;
    }

    public void toFile(String path) throws Exception {
        try {
            DOMSource source = new DOMSource(doc);

            File file = new File(path);
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
            StreamResult result = new StreamResult(outputStream);
            transf.transform(source, result);

            outputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    public String toString(Document doc) throws Exception {
        try {
            DOMSource source = new DOMSource(doc);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            StreamResult result = new StreamResult(outputStream);
            transf.transform(source, result);

            return outputStream.toString();
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }
}
