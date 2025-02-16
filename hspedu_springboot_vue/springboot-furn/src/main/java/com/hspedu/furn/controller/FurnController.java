package com.hspedu.furn.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hspedu.furn.bean.Furn;
import com.hspedu.furn.service.FurnService;
import com.hspedu.furn.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

/**
 * @author ZhouYu
 * @version 1.0
 * 说明
 * 1. 因为当前项目是前后端分离的，在默认情况下，前端发出请求
 * 2. 后端，返回一个json数据，为了方便，我们就在类上使用@RestController
 */
@RestController
@Slf4j
public class FurnController {

    // 装配Service
    @Resource
    private FurnService furnService;

    //编写方法，完成添加
    //说明
    //1. 我们前端如果是以json格式来发送添加信息furn，那么我们需要使用@RequestBody，
    //  ，才能将数据封装到对应的bean，同时保证我们http的请求头 content-type是对应的
    //2. 如果前端是以表单形式提交的，则不需要使用@RequestBody，才会进行对象参数的封装，同时要保证
    //   http的请求头 content-type是对应的

    // 编写方法，完成添加
    @PostMapping("/save")
    public Result save(@Validated @RequestBody Furn furn, Errors errors) {

        //如果出现校验错误，springboot 底层会把错误信息，封装到errors

        //定义map，准备把errors中的校验错误放入到map，如果有错误信息
        //就不真正添加，并且将错误信息通过map返回给客户端-客户端就可以取出显示
        HashMap<String, Object> map = new HashMap<>();

        List<FieldError> fieldErrors = errors.getFieldErrors();
        //遍历 将错误信息放入到map，当然可能有，也可能没有错误
        for (FieldError fieldError : fieldErrors) {
            map.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        if (map.isEmpty()) {//说明没有校验错误,正常添加
            log.info("furn={}", furn);
            furnService.save(furn);
            return Result.success(); // 返回成功信息
        } else {
            return Result.error("400", "后端校验失败~", map);
        }


    }

    // 返回所有的家居信息
    @RequestMapping("/furns")
    public Result listFurns() {
        List<Furn> furns = furnService.list();
        return Result.success(furns);
    }

    // 处理修改

    /**
     * 说明：
     * 1. @PutMapping 我们使用Rest风格，因为这里是修改的请求，使用put请求
     * 2. @RequestBody：表示前端/客户端 发生的数据是以json格式来发送
     *
     * @param furn
     * @return
     */
    @PutMapping("/update")
    public Result update(@RequestBody Furn furn) {
        //这个updateById是mybatis-plus提供的
        furnService.updateById(furn);
        return Result.success();
    }

    // 处理删除

    // 使用url占位符+@PathVariable 配合使用
    // 使用rest 风格 -> del方式
    @DeleteMapping("/del/{id}")
    public Result del(@PathVariable Integer id) {
        // 说明removeById 是Mybatis-Plus提供
        furnService.removeById(id);
        return Result.success();
    }

    // 增加方法[接口]，根据id，返回对应的家居信息
    // 依然使用url占位符+@PathVariable
    @GetMapping("/find/{id}")
    public Result findById(@PathVariable Integer id) {
        Furn furn = furnService.getById(id);
        return Result.success(furn);// 返回成功的信息-携带查询到furn信息
    }

    // 分页查询的接口/方法

    /**
     * @param pageNum  显示第几页，默认1
     * @param pageSize 每页显示几条记录，默认5
     * @return
     */
    @GetMapping("/furnsByPage")
    public Result listFurnsByPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                  @RequestParam(defaultValue = "5") Integer pageSize) {
        // 这里通过page方法，返回Page对象，对象中就封装了分页数据
        Page<Furn> page = furnService.page(new Page<>(pageNum, pageSize));
        // 这里我们注意观察，返回的page数据结构是如何的？这样才能知道在前端如何绑定返回的数据。
        return Result.success(page);
    }

    //方法：可以支持带条件的分页检索

    /**
     * @param pageNum  显示第几页
     * @param pageSize 每页显示几条记录
     * @param search   检索条件：家居名，默认是”“，表示不带条件检索，正常分页
     * @return
     */
    @GetMapping("/furnsBySearchPage")
    public Result listFurnsByConditionPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                           @RequestParam(defaultValue = "5") Integer pageSize,
                                           @RequestParam(defaultValue = "") String search) {

        // 先创建QueryWrapper，可以将我们的检索条件封装到QueryWrapper
        QueryWrapper<Furn> queryWrapper = Wrappers.query();
        // 判断search 是否有内容
        if (StringUtils.hasText(search)) {
            queryWrapper.like("name", search);
        }

        Page<Furn> page = furnService.page(new Page<>(pageNum, pageSize), queryWrapper);

        return Result.success(page);
    }


    // 编写方法，使用LambdaQueryWrapper封装查询条件，完成检索
    @GetMapping("/furnsBySearchPage2")
    public Result listFurnsByConditionPage2(@RequestParam(defaultValue = "1") Integer pageNum,
                                            @RequestParam(defaultValue = "5") Integer pageSize,
                                            @RequestParam(defaultValue = "") String search) {

        //说明：关于lambda表达式，类名::实例方法

        //解读：
        //1. Furn::getName 就是通过lambda表达式引用实例方法 getName
        //2. 这里就是把 Furn::getName 赋给了 SFunction<T,R> 函数式接口 ？
        //3. 看看 SFunction<T,R> 源码
        /**
         * @FunctionalInterface
         * public interface SFunction<T, R> extends Function<T, R>, Serializable {
         * }
         *父接口
         * @FunctionalInterface
         * public interface Function<T, R> {
         *      R apply(T t); //抽象方法：表示根据类型T的类型，获取类型R的结果
         *
         *      // 后面还有默认实现的方法
         *
         * }
         * 4. 传入 Furn::getName 后，就相当于实现了 SFunction<T, R> 的apply方法
         * 5. 底层会根据 传入的 Furn::getName 去得到该方法的对应的属性映射的表的字段，可以更加灵活
         * 6. mybatis 在xxMapper.xml 中有 ResultMap 会体现 Bean的属性和表的字段的映射关系
         *
         */




        //创建LambdaQueryWrapper，封装检索条件
        LambdaQueryWrapper<Furn> lambdaQueryWrapper = Wrappers.<Furn>lambdaQuery();

        //判断search
        if (StringUtils.hasText(search)) {

            //lambdaQueryWrapper.like(Furn::getName, search);

            SFunction<Furn,Object> sf = Furn::getName;
            lambdaQueryWrapper.like(sf, search);

        }

        Page<Furn> page = furnService.page(new Page<>(pageNum, pageSize), lambdaQueryWrapper);
        log.info("page={}", page.getRecords());
        return Result.success(page);
    }

}
