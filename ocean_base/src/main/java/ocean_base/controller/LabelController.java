package ocean_base.controller;

import ocean_user.entity.PageResult;
import ocean_user.entity.Result;
import ocean_user.entity.StatusCode;
import ocean_base.entity.Label;
import ocean_base.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/label")
@CrossOrigin
@RefreshScope
public class LabelController {
    @Autowired
    private LabelService labelService;
    @Autowired
    private HttpServletRequest httpServletRequest;

    @PostMapping()
    public Result save(@RequestBody Label label){
        labelService.save(label);
    return new Result(true, StatusCode.OK,"保存成功");
    }

    @GetMapping()
    public Result findAll(){
    String header=httpServletRequest.getHeader("Authorization");
        System.out.println(header);
        List<Label> list=labelService.findAll();
        return new Result(true,StatusCode.OK,"查询成功",list);
    }
    @GetMapping("{/labelId}")
    public Result findById(@PathVariable("labelId") String labelId){
    Label label=labelService.findById(labelId);
    return new Result(true,StatusCode.OK,"查询成功",label);
    }
    @PutMapping("{/labelId}")
    public Result updateById(@PathVariable("labelId") String labelId,@RequestBody Label label){
    label.setId(labelId);
    labelService.update(label);
    return new Result(true,StatusCode.OK,"修改成功");
    }
    @DeleteMapping("{/labelId}")
    public Result deleteById(@PathVariable("labelId") String labelId){
        labelService.delete(labelId);
        return new Result(true,StatusCode.OK,"删除成功");
        //TODO应为逻辑删除
    }
    @PostMapping("/search")
    public Result findSearch(@RequestBody Label label){
        List<Label> list = labelService.findSearch(label);
        return new Result(true,StatusCode.OK,"查询成功",list);
    }
    //分页
    @PostMapping("/search/{page}/{size}")
    public  Result pageQuery(@RequestBody Label label,@PathVariable("page") int page,@PathVariable("size") int size){
        Page<Label> pageSearch=labelService.pageQuery(label,page,size);
        return new Result(true,StatusCode.OK,"查询成功",
                new PageResult<Label>(pageSearch.getTotalElements(),pageSearch.getContent()));
    }



}
