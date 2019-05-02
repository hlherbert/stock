package com.hl.stock.core.datatype;

import com.hl.stock.common.util.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class TreeNode<T> {
    private List<TreeNode<T>> children = new ArrayList<>();
    private T value;

    public List<TreeNode<T>> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode<T>> children) {
        this.children = children;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    //递归显示并打印一棵树
    public static <T> void printTree(TreeNode<T> f, int level) {

        String preStr = "";     // 打印前缀
        for (int i = 0; i < level; i++) {
            preStr += "    ";
        }

        for (int i = 0; i < f.children.size(); i++) {
            TreeNode<T> t = f.children.get(i);
            System.err.println(preStr + "-" + JsonUtils.toJson(t.value));

            if (!t.children.isEmpty()) {
                printTree(t, level + 1);
            }
        }
    }
}
