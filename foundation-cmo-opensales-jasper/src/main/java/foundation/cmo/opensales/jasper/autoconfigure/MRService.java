package foundation.cmo.opensales.jasper.autoconfigure;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRRewindableDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/** The Constant log. */
@Slf4j
public class MRService {
	

	/** The reports path. */
	@Value("${cmo.foundation.jasper.report.path:reports}")
	private String reportsPath;

	/** The reports delemiter. */
	@Value("${cmo.foundation.jasper.report.delemiter:_&_}")
	private String reportsDelemiter;
	
	/**
	 * Pixels to millimeters.
	 *
	 * @param pixels the pixels
	 * @param dpi    the dpi
	 * @return the double
	 */
	public double pixelsToMillimeters(int pixels, int dpi) {
        double milimetersPerInch = 25.4;
        double inches = (double) pixels / dpi;
        double milimeters = inches * milimetersPerInch;
        return milimeters;
    }
	
	/**
	 * Gets the JR bean data source object.
	 *
	 * @param value the value
	 * @return the JR bean data source object
	 */
	public  JRRewindableDataSource getJRBeanDataSourceObject(Object value) {
		try {
			return new JRBeanCollectionDataSource(Arrays.asList(value));
		} catch (Exception e) {
			return new JREmptyDataSource();
		}
	}

	/**
	 * Gets the JR bean data source list.
	 *
	 * @param list the list
	 * @return the JR bean data source list
	 */
	public  JRRewindableDataSource getJRBeanDataSourceList(Collection<?> list) {
		try {
			return new JRBeanCollectionDataSource(list);
		} catch (Exception e) {
			return new JREmptyDataSource();
		}
	}
	
	 /**
	 * Gets the QR code.
	 *
	 * @param contents the contents
	 * @param w        the w
	 * @param h        the h
	 * @return the QR code
	 */
 	public Image getQRCode(String contents, int w, int h) {
	        try {

	            HashMap<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
	            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
	            QRCodeWriter qrCodeWriter = new QRCodeWriter();
	            BitMatrix byteMatrix;
	            byteMatrix = qrCodeWriter.encode(contents,
	                    BarcodeFormat.QR_CODE, w, h);

	            int matrixWidth = byteMatrix.getWidth();
	            BufferedImage image = new BufferedImage(matrixWidth, matrixWidth,
	                    BufferedImage.TYPE_INT_RGB);
	            image.createGraphics();

	            Graphics2D graphics = (Graphics2D) image.getGraphics();
	            graphics.setColor(Color.WHITE);
	            graphics.fillRect(0, 0, matrixWidth, matrixWidth);
	            graphics.setColor(Color.BLACK);

	            
	            for (int i = 0; i < matrixWidth; i++) {
	                for (int j = 0; j < matrixWidth; j++) {
	                    if (byteMatrix.get(i, j)) {
	                        graphics.fillRect(i, j, 1, 1);
	                    }
	                }
	            }

	            return image;
	        } catch (WriterException e) {
	            throw new UnsupportedOperationException(e);
	        }
	    }
	
	
	/**
	 * Gets the jasper print.
	 *
	 * @param reportName the report name
	 * @param values     the values
	 * @return the jasper print
	 * @throws Exception the exception
	 */
	public JasperPrint getJasperPrint(String reportName, List<?> values) throws Exception {
		return getJasperPrint(reportName, new HashMap<>(), values);
	}
	
	/**
	 * Gets the jasper print.
	 *
	 * @param reportName the report name
	 * @param params     the params
	 * @param values     the values
	 * @return the jasper print
	 * @throws Exception the exception
	 */
	public JasperPrint getJasperPrint(String reportName, Map<String, Object> params, List<?> values) throws Exception {
		JasperReport jr = getJasperReport(reportName);
		params.put("r", this);
		return JasperFillManager.fillReport(jr, params, new JRBeanCollectionDataSource(values));
	}
	
	/**
	 * Gets the jasper report.
	 *
	 * @param reportName the report name
	 * @return the jasper report
	 * @throws Exception the exception
	 */
	public JasperReport getJasperReport(String reportName) throws Exception {
		File jasperFile = getJasper(reportName);
		if (jasperFile == null) {
			throw new Exception(String.format("Report(%s) not found.", reportName));
		}
		return (JasperReport) JRLoader.loadObject(jasperFile);
	}
	
	/**
	 * Auto compile reports.
	 */
	public void autoCompileReports() {
		try {
			File file = new File(reportsPath);
			if (!file.exists()) {
				throw new Exception(String.format("Directory of report's(%s) not found.", file.getAbsolutePath()));
			}

			filesToCompile(file).stream().forEach(f -> {
				try {
					compileReport(f);
				} catch (Exception e) {
					log.error("Error on compile ({}): {}", f.getName(), e.getMessage());
				}
			});

			log.info("~~> Done!");

		} catch (Exception e) {
			log.error("Error: {}", e.getMessage());
		}
	}
	
	/**
	 * Compile report.
	 *
	 * @param file the file
	 * @throws Exception the exception
	 */
	public void compileReport(File file) throws Exception {
		final String name = file.getName();
		log.info("--> Compiling {}...", name);
		File jasperFile = getJasper(name);
		
		log.info("Jasper: {}", jasperFile);
		
		if (jasperFile != null) {
			jasperFile.delete();
		}
		 
		String md5 = getHashMD5(file);
		String nname = file.getName().replace(".jrxml", "");
		nname = String.format("%s%s%s.jasper", nname, reportsDelemiter, md5);
		nname = file.getAbsolutePath().replace(name, nname);
		
		JasperDesign design = JRXmlLoader.load(file);
		JasperCompileManager.compileReportToFile(design, nname);
	}

	/**
	 * Gets the jasper.
	 *
	 * @param name the name
	 * @return the jasper
	 */
	public File getJasper(String name) {
		name = name.replace(".jrxml", "");
		name = name.replace(".jasper", "");
		
		final String fname = name;
		
		File dir = new File(reportsPath);
		List<File> list = listf(dir).stream().filter(
				f -> f.getName().endsWith(".jasper")
				&& f.getName().contains(fname)).toList();
		
		for(File f : list) {
			String sname = f.getName();
			if (sname.contains(reportsDelemiter)) {
				int index = sname.indexOf(reportsDelemiter);
				sname = sname.substring(0, index);
			}else {
				sname = sname.replace(".jasper", "");
			}
			
			if (sname.equals(fname)) {
				return f;
			}
		}
		
		return null;
	}

	/**
	 * Files to compile.
	 *
	 * @param dir the dir
	 * @return the list
	 */
	public List<File> filesToCompile(File dir) {
		List<File> ret = new ArrayList<>();
		List<File> listJrxml = listf(dir).stream().filter(f -> f.getName().endsWith(".jrxml")).toList();
		List<File> listJasper = listf(dir).stream().filter(f -> f.getName().endsWith(".jasper")).toList();

		listJrxml.forEach(f -> {
			boolean incompile = true;

			String sjrxml = f.getName().replace(".jrxml", "");
			for (File f2 : listJasper) {
				String sjasper = f2.getName().replace(".jasper", "");
				if (sjasper.contains(reportsDelemiter)) {
					int index = sjasper.indexOf(reportsDelemiter);
					String md5 = sjasper.substring(index).replace(reportsDelemiter, "");
					sjasper = sjasper.substring(0, index);

					if (sjrxml.equals(sjasper)) {
						if (getHashMD5(f).equals(md5)) {
							incompile = false;
							break;
						}
					}
				}

				if (sjrxml.equals(sjasper)) {
					incompile = true;
					break;
				}
			}

			if (incompile) {
				ret.add(f);
			}
		});

		return ret;
	}
	
//	private boolean isChanged(File dir) {
//		if (sjasper.contains(reportsDelemiter)) {
//			int index = sjasper.indexOf(reportsDelemiter);
//			String md5 = sjasper.substring(index).replace(reportsDelemiter, "");
//			sjasper = sjasper.substring(0, index);
//
//			if (sjrxml.equals(sjasper)) {
//				if (getHashMD5(f).equals(md5)) {
//					incompile = false;
//					break;
//				}
//			}
//		}
//
//	}
	

	/**
 * Listf.
 *
 * @param dir the dir
 * @return the list
 */
public List<File> listf(File dir) {
		return listf(dir, new ArrayList<File>());
	}

	/**
	 * Listf.
	 *
	 * @param dir  the dir
	 * @param list the list
	 * @return the list
	 */
	public List<File> listf(File dir, List<File> list) {
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				listf(f.getAbsoluteFile(), list);
			} else {
				list.add(f);
			}
		}
		return list;
	}

	/**
	 * Gets the hash MD 5.
	 *
	 * @param file the file
	 * @return the hash MD 5
	 */
	public String getHashMD5(File file) {
		try (InputStream is = new FileInputStream(file)) {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] buffer = new byte[(int) file.length()];
			int read;
			while ((read = is.read(buffer)) > 0) {
				digest.update(buffer, 0, read);
			}

			byte[] md5sum = digest.digest();
			BigInteger bi = new BigInteger(1, md5sum);
			String out = bi.toString(16);
			return out;
		} catch (Exception ex) {
			return null;
		}

	}

}
