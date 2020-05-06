package com.aze.common.util

import cn.hutool.core.collection.CollUtil
import java.util.*


data class TreeNode<T>(
        var id: T? = null,
        var parentId: T? = null,
        var nodeName: String? = null,
        var children: Collection<TreeNode<T>>? = null) {

}

/**
 * 构造单棵树, 如果是森林只会取最开始的那棵树
 */
fun <T> constructTree(nodes: MutableCollection<TreeNode<T>>): TreeNode<T> {
    if (CollUtil.isEmpty(nodes)) return TreeNode()
    val treeNodes = LinkedHashSet<TreeNode<T>>()
    // 先获得root node
    val rootNodes = nodes.first { it.parentId == null }
    for (n in treeNodes) {
        buildChildren(n, nodes)
        treeNodes.add(n)
    }
    return rootNodes
}


/**
 * 递归构建当前节点的的孩子列表
 * @param node 当前节点
 * @param nodes 节点列表
 */
private fun <T> buildChildren(node: TreeNode<T>, nodes: MutableCollection<TreeNode<T>>) {
    val children = nodes.filter { it.parentId == node.id }
    if (CollUtil.isNotEmpty(children)) {
        children.forEach {
            buildChildren(it, nodes)
        }
    }
    node.children = children
}

