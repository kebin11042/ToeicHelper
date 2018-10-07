package com.example.bang.toeichelper;

import com.example.bang.toeichelper.mydata.TOEICDATE;
import com.example.bang.toeichelper.mydata.TOEICDATE_Handler;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.TextExtractor;

import java.util.List;

/**
 * Created by BANG on 2015-01-30.
 */


/* YBM 시험 일정 source
.25<<table>
    <tr>
        <td class="c_font">ㆍ아래 일정은 사정에 따라 변경될 수 있습니다.</td>
    </tr>
    <tr>
        <td class="c_font">ㆍ2012년 1월 TOEIC 정기시험부터 인터넷 접수만 가능합니다.</td>
    </tr>
    <!-- <tr>
        <td class="c_font">ㆍ<span class="c_fontred">2013년 10월 12일(토) 시행 TOEIC</span> 시험은 기존 시험과 달리 <span class="c_fontred">오후 2시 20분에 시행</span>됩니다.</td>
    </tr> -->
    <tr>
        <td>
            <table class="table_info_print">
                <tr>
                    <th class="str">회차</th>
                    <th class="str">시험일</th>
                    <th class="str">성적발표일</th>
                    <th class="str">인터넷 접수기간 </th>
                </tr>


                <tr>
                    <td  style="font-weight:normal;">제282회</td>
                    <td  style="font-weight:normal;">15.01span style='color:red'>(일)</span>&nbsp;오전 9시 20분</td>
                    <td  style="font-weight:normal;">15.02.13(금) 오후 3시</td>
                    <td  align = "left" style="font-weight:normal;">정기접수14.12.01(월) 오전 10시~14.12.22(월) 오전 8시<br />
                         특별추가14.12.22(월) 오전 10시~15.01.22(목) 오전 11시</td>
                </tr>

                <tr>
                    <td class = "str"  style="font-weight:bold;">제283회</td>
                    <td class = "str"  style="font-weight:bold;">15.02.08<span style='color:red'>(일)</span>&nbsp;오전 9시 20분</td>
                    <td class = "str"  style="font-weight:bold;">15.02.27(금) 오후 3시</td>
                    <td class = "str"  align = "left" style="font-weight:bold;">정기접수14.12.08(월) 오전 10시~15.01.12(월) 오전 8시<br />
                         특별추가15.01.12(월) 오전 10시~15.02.05(목) 오전 8시</td>
                </tr>

                <tr>
                    <td class = "str"  style="font-weight:bold;">제284회</td>
                    <td class = "str"  style="font-weight:bold;">15.02.28<span style='color:blue'>(토)</span>&nbsp;오전 9시 20분</td>
                    <td class = "str"  style="font-weight:bold;">15.03.19(목) 오후 3시</td>
                    <td class = "str"  align = "left" style="font-weight:bold;">정기접수14.12.22(월) 오전 10시~15.01.26(월) 오전 8시<br />
                         특별추가15.01.26(월) 오전 10시~15.02.25(수) 오전 8시</td>
                </tr>

                <tr>
                    <td class = "str"  style="font-weight:bold;">제285회</td>
                    <td class = "str"  style="font-weight:bold;">15.03.15<span style='color:red'>(일)</span>&nbsp;오전 9시 20분</td>
                    <td class = "str"  style="font-weight:bold;">15.04.03(금) 오후 3시</td>
                    <td class = "str"  align = "left" style="font-weight:bold;">정기접수15.01.12(월) 오전 10시~15.02.16(월) 오전 8시<br />
                         특별추가15.02.16(월) 오전 10시~15.03.12(목) 오전 8시</td>
                </tr>

                <tr>
                    <td class = "str"  style="font-weight:bold;">제286회</td>
                    <td class = "str"  style="font-weight:bold;">15.03.29<span style='color:red'>(일)</span>&nbsp;오전 9시 20분</td>
                    <td class = "str"  style="font-weight:bold;">15.04.17(금) 오후 3시</td>
                    <td class = "str"  align = "left" style="font-weight:bold;">정기접수15.01.26(월) 오전 10시~15.03.02(월) 오전 8시<br />
                         특별추가15.03.02(월) 오전 10시~15.03.26(목) 오전 8시</td>
                </tr>

                <tr>
                    <td  style="font-weight:normal;">제287회</td>
                    <td  style="font-weight:normal;">15.04.26<span style='color:red'>(일)</span>&nbsp;오전 9시 20분</td>
                    <td  style="font-weight:normal;">15.05.15(금) 오후 3시</td>
                    <td  align = "left" style="font-weight:normal;">정기접수15.03.02(월) 오전 10시~15.03.30(월) 오전 8시<br />
                         특별추가15.03.30(월) 오전 10시~15.04.23(목) 오전 8시</td>
                </tr>

                <tr>
                    <td  style="font-weight:normal;">제288회</td>
                    <td  style="font-weight:normal;">15.05.09<span style='color:blue'>(토)</span>&nbsp;오후 2시 20분</td>
                    <td  style="font-weight:normal;">15.05.28(목) 오후 3시</td>
                    <td  align = "left" style="font-weight:normal;">정기접수15.03.09(월) 오전 10시~15.04.13(월) 오전 8시<br />
                         특별추가15.04.13(월) 오전 10시~15.05.06(수) 오전 8시</td>
                </tr>

                <tr>
                    <td  style="font-weight:normal;">제289회</td>
                    <td  style="font-weight:normal;">15.05.31<span style='color:red'>(일)</span>&nbsp;오전 9시 20분</td>
                    <td  style="font-weight:normal;">15.06.19(금) 오후 3시</td>
                    <td  align = "left" style="font-weight:normal;">정기접수15.03.30(월) 오전 10시~15.04.27(월) 오전 8시<br />
                         특별추가15.04.27(월) 오전 10시~15.05.28(목) 오전 8시</td>
                </tr>

                <tr>
                    <td  style="font-weight:normal;">제290회</td>
                    <td  style="font-weight:normal;">15.06.28<span style='color:red'>(일)</span>&nbsp;오전 9시 20분</td>
                    <td  style="font-weight:normal;">15.07.17(금) 오후 3시</td>
                    <td  align = "left" style="font-weight:normal;">정기접수15.04.27(월) 오전 10시~15.06.01(월) 오전 8시<br />
                         특별추가15.06.01(월) 오전 10시~15.06.25(목) 오전 8시</td>
                </tr>

                <tr>
                    <td  style="font-weight:normal;">제291회</td>
                    <td  style="font-weight:normal;">15.07.11<span style='color:blue'>(토)</span>&nbsp;오후 2시 20분</td>
                    <td  style="font-weight:normal;">15.07.30(목) 오후 3시</td>
                    <td  align = "left" style="font-weight:normal;">정기접수15.05.11(월) 오전 10시~15.06.15(월) 오전 8시<br />
                         특별추가15.06.15(월) 오전 10시~15.07.08(수) 오전 8시</td>
                </tr>

                <tr>
                    <td  style="font-weight:normal;">제292회</td>
                    <td  style="font-weight:normal;">15.07.26<span style='color:red'>(일)</span>&nbsp;오전 9시 20분</td>
                    <td  style="font-weight:normal;">15.08.14(금) 오후 3시</td>
                    <td  align = "left" style="font-weight:normal;">정기접수15.06.01(월) 오전 10시~15.06.29(월) 오전 8시<br />
                         특별추가15.06.29(월) 오전 10시~15.07.23(목) 오전 8시</td>
                </tr>

                <tr>
                    <td  style="font-weight:normal;">제293회</td>
                    <td  style="font-weight:normal;">15.08.09<span style='color:red'>(일)</span>&nbsp;오전 9시 20분</td>
                    <td  style="font-weight:normal;">15.08.28(금) 오후 3시</td>
                    <td  align = "left" style="font-weight:normal;">정기접수15.06.08(월) 오전 10시~15.07.13(월) 오전 8시<br />
                         특별추가15.07.13(월) 오전 10시~15.08.06(목) 오전 8시</td>
                </tr>

                <tr>
                    <td  style="font-weight:normal;">제294회</td>
                    <td  style="font-weight:normal;">15.08.30<span style='color:red'>(일)</span>&nbsp;오전 9시 20분</td>
                    <td  style="font-weight:normal;">15.09.18(금) 오후 3시</td>
                    <td  align = "left" style="font-weight:normal;">정기접수15.06.29(월) 오전 10시~15.07.27(월) 오전 8시<br />
                         특별추가15.07.27(월) 오전 10시~15.08.27(목) 오전 8시</td>
                </tr>

                <tr>
                    <td  style="font-weight:normal;">제295회</td>
                    <td  style="font-weight:normal;">15.09.20<span style='color:red'>(일)</span>&nbsp;오전 9시 20분</td>
                    <td  style="font-weight:normal;">15.10.09(금) 오후 3시</td>
                    <td  align = "left" style="font-weight:normal;">정기접수15.07.27(월) 오전 10시~15.08.31(월) 오전 8시<br />
                         특별추가15.08.31(월) 오전 10시~15.09.17(목) 오전 8시</td>
                </tr>

                <tr>
                    <td  style="font-weight:normal;">제296회</td>
                    <td  style="font-weight:normal;">15.10.10<span style='color:blue'>(토)</span>&nbsp;오후 2시 20분</td>
                    <td  style="font-weight:normal;">15.10.29(목) 오후 3시</td>
                    <td  align = "left" style="font-weight:normal;">정기접수15.08.10(월) 오전 10시~15.09.14(월) 오전 8시<br />
                         특별추가15.09.14(월) 오전 10시~15.10.07(수) 오전 8시</td>
                </tr>

                <tr>
                    <td  style="font-weight:normal;">제297회</td>
                    <td  style="font-weight:normal;">15.10.25<span style='color:red'>(일)</span>&nbsp;오전 9시 20분</td>
                    <td  style="font-weight:normal;">15.11.13(금) 오후 3시</td>
                    <td  align = "left" style="font-weight:normal;">정기접수15.08.31(월) 오전 10시~15.09.21(월) 오전 8시<br />
                         특별추가15.09.21(월) 오전 10시~15.10.22(목) 오전 8시</td>
                </tr>

                <tr>
                    <td  style="font-weight:normal;">제298회</td>
                    <td  style="font-weight:normal;">15.11.14<span style='color:blue'>(토)</span>&nbsp;오후 2시 20분</td>
                    <td  style="font-weight:normal;">15.12.03(목) 오후 3시</td>
                    <td  align = "left" style="font-weight:normal;">정기접수15.09.14(월) 오전 10시~15.10.19(월) 오전 8시<br />
                         특별추가15.10.19(월) 오전 10시~15.11.11(수) 오전 8시</td>
                </tr>

                <tr>
                    <td  style="font-weight:normal;">제299회</td>
                    <td  style="font-weight:normal;">15.11.29<span style='color:red'>(일)</span>&nbsp;오전 9시 20분</td>
                    <td  style="font-weight:normal;">15.12.18(금) 오후 3시</td>
                    <td  align = "left" style="font-weight:normal;">정기접수15.09.21(월) 오전 10시~15.10.26(월) 오전 8시<br />
                         특별추가15.10.26(월) 오전 10시~15.11.26(목) 오전 8시</td>
                </tr>

                <tr>
                    <td  style="font-weight:normal;">제300회</td>
                    <td  style="font-weight:normal;">15.12.20<span style='color:red'>(일)</span>&nbsp;오전 9시 20분</td>
                    <td  style="font-weight:normal;">16.01.08(금) 오후 3시</td>
                    <td  align = "left" style="font-weight:normal;">정기접수15.10.26(월) 오전 10시~15.11.30(월) 오전 8시<br />
                         특별추가15.11.30(월) 오전 10시~15.12.17(목) 오전 8시</td>
                </tr>

            </table>
        </td>
    </tr>

</table>
*/

public class HTMLParsingToeicDate {

    private String data;
    private String strParsingData;
    private TOEICDATE_Handler toeicdate_handler;

    public HTMLParsingToeicDate(String data) {
        this.data = data;
        this.strParsingData = "";
        this.toeicdate_handler = new TOEICDATE_Handler();
    }

    public void ParsingData(){
        int index;
        int key;
        String strTH = "";
        String strDATE = "";
        String strAnnouncement = "";
        String strApplicationPeriod = "";
        net.htmlparser.jericho.Source source = new net.htmlparser.jericho.Source(data);
        Element element = null;

        List<Element> list = source.getAllElements(HTMLElementName.TD);

        index = 0;
        strParsingData += "--------------------font-weight:normal;-------------------\n";
        for(int i=0;i<list.size();i++){
            element = list.get(i);
            String attributevalue = element.getAttributeValue("style");  // title 태그의 속성값이 type을 찾는다
            if(attributevalue != null){
                if(attributevalue.equalsIgnoreCase("font-weight:normal;")){  // type의 값이 text 이면
                    TextExtractor textExtractor = element.getTextExtractor();  // 해당 문자값을 가져온다

                    if(index % 4 == 0){
                        strTH = textExtractor.toString();  // 가져온 값을 스트링으로 변환후
                        index++;
                    }
                    else if(index % 4 == 1){
                        strDATE = textExtractor.toString();
                        index++;
                    }
                    else if(index % 4 == 2){
                        strAnnouncement = textExtractor.toString();
                        index++;
                    }
                    else{
                        strApplicationPeriod = textExtractor.toString();
                        TOEICDATE data = new TOEICDATE(0, strTH, strDATE, strAnnouncement, strApplicationPeriod);
                        //toeicdate_handler.addToeicDate(data);
                        index++;
                    }
                }
            }
        }

        //Log.w("toeicdata_handler", "size = " + toeicdate_handler.getSize());

        list = source.getAllElements(HTMLElementName.TD);

        index = 0;
        key = 0;
        strParsingData += "\n\n-------------------font-weight:bold;-------------------\n";
        for(int i=0;i<list.size();i++){
            element = list.get(i);
            String attributevalue = element.getAttributeValue("style");  // title 태그의 속성값이 type을 찾는다
            if(attributevalue != null){
                if(attributevalue.equalsIgnoreCase("font-weight:bold;")){  // type의 값이 text 이면
                    TextExtractor textExtractor = element.getTextExtractor();  // 해당 문자값을 가져온다
                    if(index % 4 == 0){
                        strTH = textExtractor.toString();  // 가져온 값을 스트링으로 변환후
                        index++;
                    }
                    else if(index % 4 == 1){
                        strDATE = textExtractor.toString();
                        index++;
                    }
                    else if(index % 4 == 2){
                        strAnnouncement = textExtractor.toString();
                        index++;
                    }
                    else{
                        strApplicationPeriod = textExtractor.toString();
                        TOEICDATE data = new TOEICDATE(key++, strTH, strDATE, strAnnouncement, strApplicationPeriod);
                        toeicdate_handler.addToeicDate(data);
                        //가장 최신의 토익셤
                        if(index == 3){
                            toeicdate_handler.setNearestToeicDate(data);
                        }
                        index++;
                    }
                }
            }
        }
        //Log.w("toeicdata_handler", "size = " + toeicdate_handler.getSize());
    }

    public String getStrParsingData() {
        return strParsingData;
    }

    public TOEICDATE_Handler getToeicdate_handler(){return this.toeicdate_handler;}
}
