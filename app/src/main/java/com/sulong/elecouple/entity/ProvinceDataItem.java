package com.sulong.elecouple.entity;

import java.util.List;

/**
 * Created by ydh on 2016/6/13.
 */
public class ProvinceDataItem extends SimpleResult1 {

    /**
     * area_id : 1
     * area_name : 广东省
     * sub_area : [{"area_id":"35","area_name":"揭阳市","sub_area":[{"area_id":"431","area_name":"榕城区"},
     * {"area_id":"432","area_name":"揭东县"},{"area_id":"433","area_name":"揭西县"},{"area_id":"434","area_name":"惠来县"},
     * {"area_id":"435","area_name":"普宁市"},{"area_id":"3461","area_name":"asf"},{"area_id":"3462","area_name":"hhj"}]},
     * {"area_id":"36","area_name":"中山市","sub_area":[{"area_id":"436","area_name":"中山市"}]},
     * {"area_id":"37","area_name":"潮州市","sub_area":[{"area_id":"437","area_name":"湘桥区"},
     * {"area_id":"438","area_name":"潮安县"},{"area_id":"439","area_name":"饶平县"},
     * {"area_id":"3460","area_name":"ffgg"}]},{"area_id":"38","area_name":"清远市","sub_area":[{"area_id":"440","area_name":"清城区"},
     * {"area_id":"441","area_name":"佛冈县"},{"area_id":"442","area_name":"阳山县"},{"area_id":"443","area_name":"连山壮族瑶族自治县"},
     * {"area_id":"444","area_name":"连南瑶族自治县"},{"area_id":"445","area_name":"清新县"},{"area_id":"446","area_name":"英德市"},
     * {"area_id":"447","area_name":"连州市"}]},{"area_id":"39","area_name":"东莞市","sub_area":[{"area_id":"448","area_name":"东莞市"}]},{"area_id":"40","area_name":"河源市","sub_area":[{"area_id":"449","area_name":"源城区"},
     * {"area_id":"450","area_name":"紫金县"},{"area_id":"451","area_name":"龙川县"},{"area_id":"452","area_name":"连平县"},{"area_id":"453","area_name":"和平县"},{"area_id":"454","area_name":"东源县"}]},{"area_id":"41","area_name":"阳江市",
     * "sub_area":[{"area_id":"455","area_name":"江城区"},{"area_id":"456","area_name":"阳西县"},{"area_id":"457","area_name":"阳东县"},{"area_id":"458","area_name":"阳春市"}]},{"area_id":"42","area_name":"梅州市",
     * "sub_area":[{"area_id":"459","area_name":"梅江区"},{"area_id":"460","area_name":"梅县"},{"area_id":"461","area_name":"大埔县"},{"area_id":"462","area_name":"丰顺县"},{"area_id":"463","area_name":"五华县"},{"area_id":"464",
     * "area_name":"平远县"},{"area_id":"465","area_name":"蕉岭县"},{"area_id":"466","area_name":"兴宁市"}]},{"area_id":"43","area_name":"汕尾市","sub_area":[{"area_id":"467","area_name":"城区"},{"area_id":"468","area_name":"海丰县"},
     * {"area_id":"469","area_name":"陆河县"},{"area_id":"470","area_name":"陆丰市"}]},{"area_id":"44","area_name":"珠海市","sub_area":[{"area_id":"471","area_name":"香洲区"},{"area_id":"472","area_name":"斗门区"},{"area_id":"473",
     * "area_name":"金湾区"}]},{"area_id":"45","area_name":"云浮市","sub_area":[{"area_id":"474","area_name":"云城区"},{"area_id":"475","area_name":"新兴县"},{"area_id":"476","area_name":"郁南县"},{"area_id":"477","area_name":"云安县"},
     * {"area_id":"478","area_name":"罗定市"}]},{"area_id":"46","area_name":"深圳市","sub_area":[{"area_id":"479","area_name":"罗湖区"},{"area_id":"480","area_name":"福田区"},{"area_id":"481","area_name":"南山区"},{"area_id":"482",
     * "area_name":"宝安区"},{"area_id":"483","area_name":"龙岗区"},{"area_id":"484","area_name":"盐田区"},{"area_id":"485","area_name":"光明新区"},{"area_id":"486","area_name":"龙华新区"},{"area_id":"487","area_name":"坪山新区"},{"area_id":"488",
     * "area_name":"大鹏新区"}]},{"area_id":"47","area_name":"韶关市","sub_area":[{"area_id":"489","area_name":"武江区"},{"area_id":"490","area_name":"浈江区"},{"area_id":"491","area_name":"曲江区"},{"area_id":"492","area_name":"始兴县"},
     * {"area_id":"493","area_name":"仁化县"},{"area_id":"494","area_name":"翁源县"},{"area_id":"495","area_name":"乳源瑶族自治县"},{"area_id":"496","area_name":"新丰县"},{"area_id":"497","area_name":"乐昌市"},{"area_id":"498",
     * "area_name":"南雄市"}]},{"area_id":"48","area_name":"惠州市","sub_area":[{"area_id":"499","area_name":"惠城区"},{"area_id":"500","area_name":"惠阳区"},{"area_id":"501","area_name":"博罗县"},{"area_id":"502","area_name":"惠东县"},
     * {"area_id":"503","area_name":"龙门县"}]},{"area_id":"49","area_name":"广州市","sub_area":[{"area_id":"504","area_name":"荔湾区"},{"area_id":"505","area_name":"越秀区"},{"area_id":"506","area_name":"海珠区"},{"area_id":"507",
     * "area_name":"天河区"},{"area_id":"508","area_name":"白云区"},{"area_id":"509","area_name":"黄埔区"},{"area_id":"510","area_name":"番禺区"},{"area_id":"511","area_name":"花都区"},{"area_id":"512","area_name":"南沙区"},{"area_id":"513",
     * "area_name":"萝岗区"},{"area_id":"514","area_name":"增城市"},{"area_id":"515","area_name":"从化市"}]},{"area_id":"50","area_name":"湛江市","sub_area":[{"area_id":"516","area_name":"赤坎区"},{"area_id":"517","area_name":"霞山区"},
     * {"area_id":"518","area_name":"坡头区"},{"area_id":"519","area_name":"麻章区"},{"area_id":"520","area_name":"遂溪县"},{"area_id":"521","area_name":"徐闻县"},{"area_id":"522","area_name":"廉江市"},{"area_id":"523","area_name":"雷州市"},
     * {"area_id":"524","area_name":"吴川市"}]},{"area_id":"51","area_name":"江门市","sub_area":[{"area_id":"525","area_name":"蓬江区"},{"area_id":"526","area_name":"江海区"},{"area_id":"527","area_name":"新会区"},{"area_id":"528",
     * "area_name":"台山市"},{"area_id":"529","area_name":"开平市"},{"area_id":"530","area_name":"鹤山市"},{"area_id":"531","area_name":"恩平市"}]},{"area_id":"52","area_name":"佛山市","sub_area":[{"area_id":"532","area_name":"禅城区"},
     * {"area_id":"533","area_name":"南海区"},{"area_id":"534","area_name":"顺德区"},{"area_id":"535","area_name":"三水区"},{"area_id":"536","area_name":"高明区"}]},{"area_id":"53","area_name":"汕头市","sub_area":[{"area_id":"537",
     * "area_name":"龙湖区"},{"area_id":"538","area_name":"金平区"},{"area_id":"539","area_name":"濠江区"},{"area_id":"540","area_name":"潮阳区"},{"area_id":"541","area_name":"潮南区"},{"area_id":"542","area_name":"澄海区"},{"area_id":"543",
     * "area_name":"南澳县"}]},{"area_id":"54","area_name":"肇庆市","sub_area":[{"area_id":"544","area_name":"端州区"},{"area_id":"545","area_name":"鼎湖区"},{"area_id":"546","area_name":"广宁县"},{"area_id":"547","area_name":"怀集县"},
     * {"area_id":"548","area_name":"封开县"},{"area_id":"549","area_name":"德庆县"},{"area_id":"550","area_name":"高要市"},{"area_id":"551","area_name":"四会市"}]},{"area_id":"55","area_name":"茂名市","sub_area":[{"area_id":"552",
     * "area_name":"茂南区"},{"area_id":"553","area_name":"茂港区"},{"area_id":"554","area_name":"电白县"},{"area_id":"555","area_name":"高州市"},{"area_id":"556","area_name":"化州市"},{"area_id":"557","area_name":"信宜市"}]}]
     */

    public List<ProvinceData> data;

    public static class ProvinceData extends AreaItem {
        /**
         * area_id : 35
         * area_name : 揭阳市
         * sub_area : [{"area_id":"431","area_name":"榕城区"},{"area_id":"432","area_name":"揭东县"},{"area_id":"433","area_name":"揭西县"},
         * {"area_id":"434","area_name":"惠来县"},{"area_id":"435","area_name":"普宁市"},{"area_id":"3461","area_name":"asf"},
         * {"area_id":"3462","area_name":"hhj"}]
         */

        public List<CityData> sub_area;

        public static class CityData extends AreaItem {
            /**
             * area_id : 431
             * area_name : 榕城区
             */

            public List<AreaItem> sub_area;

        }

    }
}
