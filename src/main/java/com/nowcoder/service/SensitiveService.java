package com.nowcoder.service;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
    前缀树过滤敏感词
 */
@Service
public class SensitiveService implements InitializingBean {
    private void addWord(String lineTxt){
        TrieNode tempNode = rootNode;
        for(int i=0;i<lineTxt.length();i++){
            Character ch = lineTxt.charAt(i);

            if(isSymbol(ch)){
                continue;
            }

            TrieNode cur = tempNode.getSubNode(ch);
            if(cur == null){
                cur = new TrieNode();
                tempNode.addSubNode(ch,cur); //注意添加子节点的方式
            }
            tempNode = cur;
            if(i == lineTxt.length()-1){
                tempNode.setkeywordEnd(true);
            }
        }
    }

    //判断是否是一个符号
    private boolean isSymbol(char c) {
        int ic = (int) c;
        // 0x2E80-0x9FFF 东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    private class TrieNode{
        //是不是关键词的结尾
        private boolean end = false;

        //当前节点下所有的子节点
        private Map<Character,TrieNode> subNodes = new HashMap<Character,TrieNode>();

        public void addSubNode(Character key,TrieNode node){
            ((HashMap) subNodes).put(key,node);
        }

        TrieNode getSubNode(Character key){
            return  subNodes.get(key);
        }

        boolean iskeywordEnd(){
            return end;
        }

        void setkeywordEnd(boolean end){
            this.end = end;
        }
    }

    //根节点
    private TrieNode rootNode = new TrieNode();

    public String filter(String text){
        if(StringUtils.isBlank(text)){
            return text;
        }

        String replacement = "***";
        TrieNode tempNode = rootNode;
        int begin = 0,pos = 0;
        StringBuilder sb = new StringBuilder();
        while(pos < text.length()){
            if(isSymbol(text.charAt(pos))){
                if(tempNode == rootNode){
                    sb.append(text.charAt(pos));
                    begin++;
                }
                pos++;
                continue;
            }
            tempNode = tempNode.getSubNode(text.charAt(pos));

            if(tempNode==null){
                sb.append(text.charAt(begin));
                begin++;
                pos = begin;
                tempNode = rootNode;
            }else{
                if(tempNode.iskeywordEnd()){
                    begin = pos+1;
                    pos = begin;
                    sb.append(replacement);
                    tempNode = rootNode;
                }
                else{
                    pos++;
                }
            }
        }
        return sb.toString();
    }

    public static void main(String[] args){
        SensitiveService s = new SensitiveService();
        s.addWord("色情");
        System.out.print(s.filter("你 好 色 情"));
    }
}
