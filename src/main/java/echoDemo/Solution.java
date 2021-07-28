package echoDemo;

import java.util.Stack;

public class Solution {
    
    private static Stack stack = new Stack();
    private static Stack tmpStack = new Stack();



    public void push(int node) {
        stack.push(node);
        //辅助栈推入的时候比较推入数和当前栈顶大小
        if(!tmpStack.empty() && node > (Integer)tmpStack.peek() ){
            tmpStack.push((Integer)tmpStack.peek());
        }else{
            tmpStack.push(node);
        }
    }
    
    public void pop() {
       stack.pop();
       tmpStack.pop();
    }
    
    public int top() {
        return (Integer)stack.peek();
    }
    
    public int min() {
        return (Integer)tmpStack.peek();
    }
}