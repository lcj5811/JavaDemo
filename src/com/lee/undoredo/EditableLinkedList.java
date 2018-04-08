package com.lee.undoredo;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName com.lee.undoredo.EditableLinkedList
 * @description 撤销恢复操作功能实现
 * @author 凌霄
 * @data 2016年5月31日 上午9:44:08
 */
public class EditableLinkedList<T> {

	// 主列表
	private LinkedList<T> primaryList = new LinkedList<T>();

	// 快照列表
	private LinkedList<List> snapshotList = new LinkedList<List>();

	// 上一次操作调用的方法
	private Method lastOperation;

	// 上一次操作调用的方法参数
	private Object[] lastOperationArgs;

	// 拍快照，每操作一次拍下快照
	private void takeSnapshot() {
		List<T> snapshot = new LinkedList<T>();
		for (T element : primaryList) {
			snapshot.add(element);
		}
		snapshotList.add(snapshot);
	}

	// 从最新的快照恢复
	private void restoreFromSnapshot() {
		// pollLast()获取并移除此列表的最后一个元素；如果此列表为空，则返回 null。
		List<T> snapshot = snapshotList.pollLast();
		primaryList.clear();
		for (T element : snapshot) {
			primaryList.add(element);
		}
	}

	// 设置上次操作
	private void setLastInvokedOperation(String methodName, Object... params) {
		try {
			this.lastOperation = this.getClass().getMethod(methodName, this.getParamTypes(params));
		} catch (Exception e) {
			if (setLastMethodParamsGeneric(methodName, params) == false) {
				e.printStackTrace();
			}
		}
		this.lastOperationArgs = params;
	}

	// 设置上一个方法的参数
	private boolean setLastMethodParamsGeneric(String methodName, Object... params) {
		Method[] methods = this.getClass().getMethods();
		for (int i = 0; i < methods.length; i++) {
			if (this.isMethodMatch(methods[i], methodName, params)) {
				this.lastOperation = methods[i];
				return true;
			}
		}
		return false;
	}

	// 判断方法是否匹配
	private boolean isMethodMatch(Method method, String methodName, Object... params) {
		if (!method.getName().equalsIgnoreCase(methodName)) {
			return false;
		}
		Class<?>[] paramTypes = method.getParameterTypes();
		if (paramTypes.length != params.length) {
			return false;
		}
		for (int i = 0; i < params.length; i++) {
			if (!paramTypes[i].isAssignableFrom(params[i].getClass())) {
				return false;
			}
		}
		return true;
	}

	// 获取参数的类型
	private Class<?>[] getParamTypes(Object... params) {
		Class<?>[] paramTypes = new Class<?>[params.length];
		for (int i = 0; i < params.length; i++) {
			paramTypes[i] = params[i].getClass();
		}
		return paramTypes;
	}

	// 执行上一个方法
	private void invokeLastMethod() {
		try {
			this.lastOperation.invoke(this, this.lastOperationArgs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 删除操作
	public void delete(Integer beginIndex, Integer endIndex) {
		// 删除操作之前拍下快照
		this.takeSnapshot();
		for (int i = beginIndex; i <= endIndex; i++) {
			primaryList.remove(beginIndex);
		}
		this.setLastInvokedOperation("delete", beginIndex, endIndex);
	}

	// 插入操作
	public void insert(T element, Integer index) {
		// 插入操作之前拍下快照
		this.takeSnapshot();
		primaryList.add(index, element);
		this.setLastInvokedOperation("insert", element, index);
	}

	// 修改操作
	public void modify(T element, Integer index) {
		// 修改操作之前拍下快照
		this.takeSnapshot();
		primaryList.set(index, element);
		this.setLastInvokedOperation("modify", element, index);
	}

	// 重做，取消复原
	public void redo() {
		// 执行上次操作的方法
		this.invokeLastMethod();
	}

	// 撤销
	public void undo() {
		// 重最新的快照中恢复
		this.restoreFromSnapshot();
	}

	public String toString() {
		return this.primaryList.toString();
	}

	public static void main(String[] args) {
		EditableLinkedList<String> ell = new EditableLinkedList<String>();
		ell.insert("hey", 0);
		System.out.println(ell);
		ell.redo();
		System.out.println(ell);
		ell.undo();
		System.out.println(ell);
		ell.redo();
		System.out.println(ell);
		ell.undo();
		System.out.println(ell);
		ell.undo();
		System.out.println(ell);
	}
}
