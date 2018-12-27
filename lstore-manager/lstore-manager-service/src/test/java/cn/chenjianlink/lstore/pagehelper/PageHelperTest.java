package cn.chenjianlink.lstore.pagehelper;

import cn.chenjianlink.lstore.mapper.TbItemMapper;
import cn.chenjianlink.lstore.pojo.TbItem;
import cn.chenjianlink.lstore.pojo.TbItemExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;


public class PageHelperTest {

    @Test
    public void testPageHelper() throws Exception {
        //初始化容器信息
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/springconfig-dao.xml");
        //获得代理对象
        TbItemMapper itemMapper = applicationContext.getBean(TbItemMapper.class);
        //设置分页信息
        PageHelper.startPage(1, 10);
        //执行查询
        TbItemExample example = new TbItemExample();
        List<TbItem> list = itemMapper.selectByExample(example);
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        System.out.println(pageInfo.getTotal());
        System.out.println(pageInfo.getPages());
        System.out.println(list.size());

    }
}
