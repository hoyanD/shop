package shop

import grails.transaction.Transactional
import net.sf.jasperreports.engine.JREmptyDataSource
import net.sf.jasperreports.engine.JasperCompileManager
import net.sf.jasperreports.engine.JasperExportManager
import net.sf.jasperreports.engine.JasperFillManager
import net.sf.jasperreports.engine.JasperPrint
import net.sf.jasperreports.engine.JasperReport

@Transactional
class ReportsService {

    def servletContext

    def serviceMethod(Shopping item) {
        Map model = new HashMap()

        model.put("address", item.getAddress())
        model.put("phone", item.getPhone())
        model.put("date", item.getDate().toString())
        model.put("price", item.getPrice().toString())
        model.put("person", item.getPerson())
        model.put("goods", item.getGoods().getName())

//        JRPropertiesUtil

        /*JRProperties.setProperty("net.sf.jasperreports.default.pdf.font.name", "Helvetica")
        JRProperties.setProperty("net.sf.jasperreports.default.pdf.encoding", "UTF8")
        JRProperties.setProperty("net.sf.jasperreports.default.pdf.embedded", "false")*/

        JasperReport jasperReport = JasperCompileManager.compileReport(servletContext.getRealPath("/") + "reports/ua/goods.jrxml")

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, model, new JREmptyDataSource())

        JasperExportManager.exportReportToHtmlFile(jasperPrint, servletContext.getRealPath("/") + "reports/printed-files/" + item.getGoods().getName() + ".html")
    }
}