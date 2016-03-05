package com.magus.enviroment.ep.activity.knowledge;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magus.enviroment.R;
import com.magus.enviroment.ui.CustomActionBar;
import com.magus.enviroment.ui.swipefinish.app.SwipeBackActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 环保百科
 */
public class KnowledgeActivity extends SwipeBackActivity {
    private CustomActionBar mActionBar;
    private Drawable img_down, img_up;
    private ExpandableListView mExpandList;
    private List<String> parent;
    private Map<String, List<String>> map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge);
        initActionBar();
        initData();
        initView();


        img_down = getResources().getDrawable(R.mipmap.group_down);
        img_down.setBounds(0, 0, img_down.getMinimumWidth(), img_down.getMinimumHeight());
        img_up = getResources().getDrawable(R.mipmap.group_up);
        img_up.setBounds(0, 0, img_up.getMinimumWidth(), img_up.getMinimumHeight());
    }

    private void initView() {
        mExpandList = (ExpandableListView) findViewById(R.id.main_expandablelistview);
        mExpandList.setGroupIndicator(null);
        mExpandList.setAdapter(new MyAdaper());
        mExpandList.setOnChildClickListener(new MyChildClickListener());
    }

    private void initData() {
        parent = new ArrayList<String>();
        parent.add("大气");
        parent.add("水");

        map = new HashMap<String, List<String>>();

        List<String> list1 = new ArrayList<String>();
        list1.add("雾和霾");
        list1.add("什么是AQI");
        list1.add("其他污染物");
        map.put("大气", list1);

        List<String> list2 = new ArrayList<String>();
        list2.add("水质分类");
        list2.add("水污染源");
        list2.add("其他污染物");
        map.put("水", list2);
    }


    private void initActionBar() {
        mActionBar = (CustomActionBar) findViewById(R.id.custom_action_bar);
        mActionBar.setLeftImageClickListener(KnowledgeActivity.this);
        mActionBar.setActionBarBackground(getResources().getColor(R.color.attention_action_bar_background));
    }


    class MyAdaper extends BaseExpandableListAdapter {


        @Override
        public int getChildrenCount(int groupPosition) {
            String key = parent.get(groupPosition);
            int size = map.get(key).size();
            return size;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            String key = parent.get(groupPosition);

            return (map.get(key).get(childPosition));
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            String key = KnowledgeActivity.this.parent.get(groupPosition);
            String info = KnowledgeActivity.this.map.get(key).get(childPosition);
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) KnowledgeActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.layout_children, null);
                holder.layout = (LinearLayout) convertView.findViewById(R.id.ll_children);
                holder.tv = (TextView) convertView.findViewById(R.id.second_textview);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv.setText(info);
            return convertView;
        }


        @Override
        public Object getGroup(int groupPosition) {

            return KnowledgeActivity.this.parent.get(groupPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public int getGroupCount() {
            return KnowledgeActivity.this.parent.size();
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            String info = KnowledgeActivity.this.parent.get(groupPosition);
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) KnowledgeActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.layout_parent, null);
                holder.layout = (LinearLayout) convertView.findViewById(R.id.ll_parent);
                holder.tv = (TextView) convertView.findViewById(R.id.parent_textview);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv.setText(info);
            if (isExpanded) {
                holder.tv.setCompoundDrawables(null, null, img_up, null);

            } else {
                holder.tv.setCompoundDrawables(null, null, img_down, null);
            }

            return convertView;
        }


        @Override
        public boolean hasStableIds() {
            return true;
        }


        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        class ViewHolder {
            LinearLayout layout;
            TextView tv;
        }
    }

    class MyChildClickListener implements ExpandableListView.OnChildClickListener {

        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            String key = KnowledgeActivity.this.parent.get(groupPosition);
            String info = KnowledgeActivity.this.map.get(key).get(childPosition);
            Intent intent = new Intent();
            intent.setClass(KnowledgeActivity.this, DetailKnowledgeActivity.class);
            intent.putExtra("flag","txt");
            if (key.equals("大气")) {

                switch (childPosition) {
                    case 0:
                        intent.putExtra("title", "雾和霾");
                        intent.putExtra("text", "存在形态的区别:\n\n" +
                                "雾:雾是悬浮于空气中的水滴小颗粒。" +
                                "霾:霾是悬浮于空气中的固体小颗粒，包括灰尘、硫酸、硝酸等各种化合物。\n\n" +
                                "颜色不同：\n\n" +
                                "雾:雾是由小水滴构成，由于其物理特性，散射的光与波长关系不大，因此雾呈乳白色，青白色。" +
                                "霾:霾是由各种化合物构成，由于其物理特性，散射波长较长的光比较多，黄色，橙灰色。\n\n" +
                                "能见度不同：\n\n" +
                                "雾:由于雾越接近地面的地方密度越大，对光线的影响也越大，能见度很低，一般在1公里之内。" +
                                "霾:霾在空气中均匀分布，颗粒较小，密度较低，对光线有一定影响，但影响没有雾大，能见度较低，一般在十公里之内。\n\n" +
                                "社会影响不同：\n\n" + "雾：雾是悬浮在空中的微小水滴，过一段时间会降落到地面，对人们生活、健康影响不大。" +
                                "霾：霾是各种化合物的小微粒，对人体健康和植物都有害。\n\n" + "雾的形成:\n\n" +
                                "在水汽充足、微风及大气层稳定的情况下，气温接近零点，相对湿度达到100%时，空气中的水汽便会凝结成细微的水滴悬浮于空中，使地面水平的能见度下降，这种天气现象称为雾。雾的出现以春季二至四月间较多。\n\n" +
                                "霾的形成:\n\n" + "指原因不明的大量烟、尘等微粒悬浮而形成的浑浊现象。霾的核心物质是空气中悬浮的灰尘颗粒，气象学上称为气溶胶颗粒。\n\n" +
                                "霾的源头:\n\n" + "第一：是汽车尾气。使用柴油的大型车是排放PM10的“重犯”，包括大公交、各单位的班车，以及大型运输 卡车等。城市有毒颗粒物来源：首先是汽车尾气。使用柴油的车子是排放细颗粒物的“重犯”。使用汽油的小型车虽然排放的是气态污染物，比如氮氧化物等，但碰上雾天，也很容易转化为二次颗粒污染物，加重雾霾。" +
                                "第二：北方到了冬季烧煤供暖所产生的废气。" +
                                "第三：工业生产排放的废气。比如冶金、窑炉与锅炉、机电制造业，还有大量汽修喷漆、建材生产窑炉燃烧排放的废气。" +
                                "第四：建筑工地和道路交通产生的扬尘。" +
                                "第五：可生长颗粒，细菌和病毒的粒径相当于PM0.1-PM2.5，空气中的湿度和温度适宜时，微生物会附着在颗粒物上，特别是油烟的颗粒物上，微生物吸收油滴后转化成更多的微生物，使得雾霾中的生物有毒物质生长增多。" +
                                "第六、家庭装修中也会产生粉尘“雾霾”，室内粉尘弥漫，不仅有害于工人与用户健康，增添清洁负担，粉尘严重时，还给装修工程带来诸多隐患。");
                        startActivity(intent);
                        break;
                    case 1:
                        intent.putExtra("title", "什么是AQI");
                        intent.putExtra("text", "\t\tAQI是指空气质量指数，英文名词为Air Quality Index，定量描述空气质量状况的无量纲指数。针对单项污染物的还规定了空气质量分指数。参与空气质量评价的主要污染物为细颗粒物、可吸入颗粒物、二氧化硫、二氧化氮、臭氧、一氧化碳等六项。\n\n" +
                                "空气质量指数级别\n\n" +
                                "0－50，空气质量级别为一级，空气质量状况属于优，空气质量令人满意，基本无空气污染；" +
                                "51－100，空气质量级别为二级，空气质量状况属于良，空气质量可接受，但某些污染物可能对极少数异常敏感人群健康有较弱影响；" +
                                "101－150，空气质量级别为三级，空气质量状况属于轻度污染，易感人群症状有轻度加剧，健康人群出现刺激症状；" +
                                "151－200，空气质量级别为四级，空气质量状况属于中度污染。此时，进一步加剧易感人群症状，可能对健康人群心脏、呼吸系统有影响；" +
                                "201－300，空气质量级别为五级，空气质量状况属于重度污染。此时，心脏病和肺病患者症状显著加剧，运动耐受力降低，健康人群普遍出现症状；" +
                                "大于300，空气质量级别为六级，空气质量状况属于严重污染，健康人群运动耐受力降低，有明显强烈症状，提前出现某些疾病。\n\n"+
                                "SO2\n\n"+"二氧化硫（SO2）是无色有刺激性嗅觉的气体，易溶于水。在催化剂作用下，易被氧化为三氧化硫，遇水即可变成硫酸。大气中二氧化硫主要来源于含硫金属矿的冶炼、含硫煤和石油的燃烧所排放的废气。" +
                                "二氧化硫是大气中最常见的污染物之一。二氧化硫对人的呼吸器官和眼膜具有刺激作用，吸入高浓度二氧化硫可发生喉头水肿和支气管炎。长期吸入二氧化硫会发生慢性中毒，不仅使呼吸道疾病加重，而且对肝、肾、心脏都有危害。另外，大气中二氧化硫对植物、动物和建筑物都有危害，特别是二氧化硫在大气中经阳光照射以及某些金属粉尘（如工业烟尘中氧化铁）的催化作用，很容易氧化成三氧化硫，与空气中水蒸气结合即成硫酸雾，严重腐蚀金属制品及建筑物，并使土壤和江河湖泊日趋酸化。" +
                                "国家环境质量标准规定， 居住区二氧化硫日平均浓度低于0.15毫克/立方米，年平均浓度低于0.06毫克/立方米。\n\n"+
                                "PM10\n\n"+"指悬浮在空气中，能进入人体的呼吸系统、空气动力学当量直径≤10微米的颗粒物，即粒径在10微米以下的浮游状颗粒物，记作PM10，简写为IP。可吸入颗粒物的浓度以每立方米空气中可吸入颗粒物的毫克数表示。国家环保总局1996年颁布修订的《环境空气质量标准（GB 3095-1996）》中将飘尘改称为可吸入颗粒物，作为正式大气环境质量标准。" +
                                "国家环境质量标准规定，居住区可吸入颗粒物日平均浓度低于0.15毫克/立方米，年平均浓度低于0.1毫克/立方米。\n\n"+
                                "NOx\n\n"+"指空气中主要以一氧化氮和二氧化氮形式存在的氮的氧化物。空气中含氮的氧化物有一氧化二氮(N2O) 、一氧化氮(NO)、二氧化氮(NO2) 、三氧化二氮(N2O3)等，其中占主要成分的是一氧化氮和二氧化氮，以NOx(氮氧化物)表示。" +
                                "NOx污染主要来源于生产、生活中所用的煤、石油等燃料燃烧的产物 (包括汽车及一切内燃机燃烧排放的NOx) ；其次是来自生产或使用硝酸的工厂排放的尾气。当NOx与碳氢化物共存于空气中时，经阳光紫外线照射，发生光化学反应，产生一种光化学烟雾，它是一种有毒性的二次污染物。NOx对动物的影响浓度大致为1.0毫克/立方米，对患者的影响浓度大致为0.2毫克/立方米。" +
                                "国家环境质量标准规定，居住区氮氧化物的平均浓度低于0.10毫克/立方米，年平均浓度低于0.05毫克/立方米。\n\n"+
                                "CO\n\n"+"标准状况下，一氧化碳（carbon monoxide, CO）纯品为无色、无臭、无刺激性的气体。与空气混合爆炸极限为12.5%～74.2%。一氧化碳进入人体之后极易与血液中的血红蛋白结合，产生碳氧血红蛋白，进而使血红蛋白不能与氧气结合，使人缺氧，严重时死亡。最常见的一氧化碳中毒症状，如头痛，恶心，呕吐，头晕，疲劳和虚弱的感觉。" +
                                "由于世界各国交通运输事业、工矿企业不断发展，煤和石油等燃料的消耗量持续增长，一氧化碳的排放量也随之增多。据1970年不完全统计，全世界一氧化碳总排放量达3.71亿吨。其中汽车废气的排出量占2.37亿吨，约占64%，成为城市大气日益严重的污染来源。\n\n"+
                                "O3\n\n"+"在常温下，它是一种有特殊臭味的淡蓝色气体。臭氧主要存在于距地球表面20~35公里的同温层下部的臭氧层中。在常温常压下，稳定性较差，可自行分解为氧气。臭氧具有青草的味道，吸入少量对人体有益，吸入过量对人体健康有一定危害。" +
                                "同铅污染、硫化物等一样，它也是源于人类活动，汽车、燃料、石化等是臭氧的重要污染源。在车水马龙的街上行走，常常看到空气略带浅棕色，又有一股辛辣刺激的气味，这就是通常所称的光化学烟雾。臭氧就是光化学烟雾的主要成分，它不是直接被排放的，而是转化而成的，比如汽车排放的氮氧化物，只要在阳光辐射及适合的气象条件下就可以生成臭氧。" +
                                "我国卫生部1979年制定的《工业卫生标准》中规定，臭氧的安全标准为0.15ppm。\n\n"+
                                "PM2.5\n\n"+"PM2.5是指大气中直径小于或等于2.5微米的颗粒物，也称为可入肺颗粒物。可吸入颗粒物的浓度以每立方米空气中可吸入颗粒物的毫克数表示。" +
                                "PM2.5产生的主要来源，是日常发电、工业生产、汽车尾气排放等过程中经过燃烧而排放的残留物，大多含有重金属等有毒物质。一般而言，粒径2.5微米至10微米的粗颗粒物主要来自道路扬尘等；2.5微米以下的细颗粒物(PM2.5)则主要来自化石燃料的燃烧(如机动车尾气、燃煤)、挥发性有机物等。");
                        startActivity(intent);
                        break;
                    case 2:
                        intent.putExtra("title", "其他污染物");
                        intent.putExtra("text","硫化氢（H2S）\n\n"+"硫化氢（H2S）是某些工业生产过程中产生的废气，或由含硫有机物腐败后生产。在制造硫化染料及人造纤维、制革、制药、含硫金属矿石的开采和冶炼、含硫石油的开采和加工、含硫橡胶加热等工序中，均可产生大量硫化气体。接触者大量吸入可致急性中毒。" +
                                "硫化氢是一种神经毒剂，亦为窒息性和刺激性气体。其毒作用的主要靶器官是中枢神经系统和呼吸系统，亦可伴有心脏等多器官损害。\n\n"+
                                "碳氢化合物\n\n"+"碳氢化合物是汽车发动机中燃烧不完全造成的，汽油还可以通过挥发作用，使碳氢化合物散发到空气中造成污染。" +
                                "许多碳氢化合物都是有毒的，其中一些碳氢化合物是致癌物。" +
                                "碳氢化合物包括少量醛类和多环芳烃。其中甲醛和丙烯醛对鼻、眼和呼吸黏膜有刺激作用，能引起结膜炎、鼻炎、支气管炎等疾病，而且还有难闻的臭味。此外，在强烈阳光照射下且存在氮氧化物时，碳氢化合物会发生反应，在近地面生产臭氧，有可能导致光化学烟雾污染。"
                                );
                        startActivity(intent);
                        break;
                }

            } else if (key.equals("水")) {

                switch (childPosition) {
                    case 0:
                        intent.putExtra("title", "水质分类");
                        intent.putExtra("text","\t\tⅠ~Ⅱ类\t\t优n\n"+"饮用水源地一级保护区、珍稀水生生物栖息地、鱼虾类产卵场、仔稚幼鱼的索饵场等\n\n"+
                                        "\t\tⅢ类\t\t良好\n\n"+"饮用水源地二级保护区、鱼虾类越冬场、洄游通道、水产养殖区、游泳区。\n\n"+
                                        "\t\tⅣ类\t\t轻度污染\n\n"+"主一般工业用水和人体非直接接触的娱乐用水。\n\n"+
                                        "\t\tⅤ类\t\t中度污染\n\n"+"农业用水及一般景观用水。\n\n"+
                                        "\t\t劣Ⅴ类\t\t重度污染\n\n"+"除调节局部气候外，使用功能较差");
                        startActivity(intent);
                        break;
                    case 1:
                        intent.putExtra("title", "水污染源");
                        intent.putExtra("text","生活废水\n\n"+"是对水体产生污染的最主要污染源。它指的是工业企业排出的生产过程中使用过的废水。根据污染物的性质，工业废水可分为：⑴含有机物废水，如造纸、制糖、食品加工、染织工业等废水；⑵含无机物废" +
                                "水，如火力发电厂的水力冲灰废水，采矿工业的尾矿水以及采煤炼焦工业的洗煤水等；⑶含有毒的化学性物质废水，如化工、电镀、冶炼等工业废水；⑷含有病原体工业废水，如生物制品、制革、屠宰厂废水；⑸含有放射性物质废水，如原子能发电" +
                                "厂、放射性矿、核燃料加工厂废水；⑹生产用冷却水，如热电厂、钢厂废水。\n\n"+
                                "工业废水\n\n"+"是对水体产生污染的最主要污染源。它指的是工业企业排出的生产过程中使用过的废水。根据污染物的性质，工业废水可分为：⑴含有机物废水，如造纸、制糖、食品加工、染织工业等废水；⑵含无机物废水，如火力发电厂的水力冲灰废水，采矿工业的尾矿水以及采煤炼焦工业的洗煤水等；⑶含有毒的化学性物质废水，如化工、电镀、冶炼等工业废水；⑷含有病原体工业废水，如生物制品、制革、屠宰厂废水；⑸含有放射性物质废水，如原子能发电厂、放射性矿、核燃料加工厂废水；⑹生产用冷却水，如热电厂、钢厂废水。\n\n"+
                                "农业废水\n\n"+"农业污染源主要指的是农药和化肥的不正确使用所造成的污染。如长期滥用有机氯农药和有机汞农药，污染地表水，会使水生生物、鱼贝类有较高的农药残留，加上生物富集，如食用会危害人类的健康和生命。"
                                );
                        startActivity(intent);
                        break;
                    case 2:
                        intent.putExtra("title", "其他污染物");
                        intent.putExtra("text","\t\t工业生产过程中产生的固体废弃物含有大量的易溶于水的无机和有机物，受雨水冲淋造成水体污染。" +
                                "油轮漏油或者发生事故（或突发事件）引起石油对海洋的污染，因油膜覆盖水面使水生生物大量死亡，死亡的残体分解可造成水体污染。\n");
                        startActivity(intent);
                        break;
                }
            }
            return false;
        }
    }
}
