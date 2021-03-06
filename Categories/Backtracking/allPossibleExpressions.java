/**
* Given a string that contains only digits from 0 to 9, and an integer value, target. 
* Find out how many expressions are possible which evaluate to target using binary operator +, – and * in given string of digits.
* 
* Input : "123",  Target : 6
* Output : {“1+2+3”, “1*2*3”}
* 
* Input : “125”, Target : 7
* Output : {“1*2+5”, “12-5”}
*/

/*
backtracking
为什么不需要循环? -> 每个数字都要用上,不需要跳过某些数字. 
对当前数字cur，每次有三种选择： cur,cur+,cur-,即什么都不加，加号，减号.对应三种不同的递归

得到表达式之后，对表达式求值需要用到javascript的API

followup：如果要求支持除法的话，engine.eval()结果可能出现double类型 需要多做一步判断
engine.eval()返回类型为Object，需要强制类型转换成需要的类型
*/
 
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
 
public class AllPossibleExpressions {
  
  public void dfs(int[] nums, List<String> res, StringBuilder tmp, int idx, int target) throws ScriptException {
    if (idx == nums.length) {
      ScriptEngineManager mgr = new ScriptEngineManager();
      ScriptEngine engine = mgr.getEngineByName("JavaScript");
      if ((int)engine.eval(tmp.toString()) == target) {
        res.add(tmp.toString());
      }
      return;
    }

    tmp.append(nums[idx]);
    dfs(nums, res, tmp, idx+1, target);
    tmp.deleteCharAt(tmp.length()-1);
    
    // 注意这里多一个判断.如果处理到了最后一个数字,回溯后直接返回,回到上一层再添加<数字+运算符>
    // 否则会出现123+这种表达式
    if (idx == nums.length - 1) return;

    tmp.append(nums[idx] + "+");
    dfs(nums, res, tmp, idx+1, target);
    tmp.deleteCharAt(tmp.length()-1);
    tmp.deleteCharAt(tmp.length()-1);

    tmp.append(nums[idx] + "-");
    dfs(nums, res, tmp, idx+1, target);
    tmp.deleteCharAt(tmp.length()-1);
    tmp.deleteCharAt(tmp.length()-1);

    tmp.append(nums[idx] + "*");
    dfs(nums, res, tmp, idx+1, target);
    tmp.deleteCharAt(tmp.length()-1);
    tmp.deleteCharAt(tmp.length()-1);
  }
  
}

