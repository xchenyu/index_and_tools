package com.example.BPlusTest;

import com.alibaba.fastjson.JSON;

import java.util.*;

/**
 * 实现B+树
 *
 * @param <T> 指定值类型
 * @param <V> 使用泛型，指定索引类型,并且指定必须继承Comparable
 */
public class BPlusTreePlanter<T, V extends Comparable<V>>{
    //B+树的阶
    private Integer bTreeOrder;
    //B+树的非叶子节点最小拥有的子节点数量（同时也是键的最小数量）
    //private Integer minNUmber;
    //B+树的非叶子节点最大拥有的节点数量（同时也是键的最大数量）
    private Integer maxNumber;

    public Node<T, V> root;

    private LeafNode<T, V> left;

    //无参构造方法，默认阶为3
    public BPlusTreePlanter(){
        this(3);
    }

    //有参构造方法，可以设定B+树的阶
    public BPlusTreePlanter(Integer bTreeOrder){
        this.bTreeOrder = bTreeOrder;
        //this.minNUmber = (int) Math.ceil(1.0 * bTreeOrder / 2.0);
        //因为插入节点过程中可能出现超过上限的情况,所以这里要加1
        this.maxNumber = bTreeOrder + 1;
        this.root = new LeafNode<T, V>();
        this.left = null;
    }
    //
    public BPlusTreePlanter(Integer bTreeOrder, Node root){
        this.bTreeOrder=bTreeOrder;
        this.maxNumber = bTreeOrder + 1;
        this.left = null;
        this.root = root;
    }

    //查询
    public List<T> find(V key){
        List<T> t = this.root.find(key);
        if(t == null){
            System.out.println("不存在");
        }
        return t;
    }

//    //范围查询
    public List<T> Rangefind(V min, Boolean isOpen1, V max, Boolean isOpen2){
        //0表示左边界，1表示右边界
        List list1=this.findboundary(0,min);
        List list2=this.findboundary(1,max);
        Node node1=(Node) list1.get(0);
        Integer index1=(Integer) list1.get(1);
        Node node2=(Node) list2.get(0);
        Integer index2=(Integer) list2.get(1);
        List<T> ret=new ArrayList<>();
        //左右边界属于同一节点
        if(node1==node2){
            for (int i=index1;i<=index2;i++){
                if((isOpen1 && (node1.keys[i]==min))) continue;
                if((isOpen2 && (node1.keys[i]==max))) continue;
                ret.add((T) node1.values.get(i));
            }
            return ret;
        }

        for (int i=index1;i<node1.number;i++){
            if((isOpen1 && (node1.keys[i]==min))) continue;
            ret.add((T) node1.values.get(i));
        }

        //中间节点符合条件的值插入列表
        Node temp=node1.right;
        while(temp!=node2 && (temp!=null) && temp.left!=node2){
            for (int i=0;i<temp.number;i++){
                ret.add((T) temp.values.get(i));
            }
            temp=temp.right;
        }

        //右边界节点符合条件的值插入列表,当且仅当右边界为闭时插入
        if(node1!=node2){
            for (int j=0;j<=index2;j++){
                if(isOpen2 && (node2.keys[j]==max)) continue;
                ret.add((T) node2.values.get(j));
            }
        }
        return ret;
    }
    //    先找到左右边界
    public List findboundary(Integer des, V key){
        return this.root.findboundary(des,key);
    }

    //插入
    public void insert(T value, V key){
        if(key == null)
            return;
        Node<T, V> t = this.root.insert(value, key);
        if(t != null)
            this.root = t;
        this.left = (LeafNode<T, V>)this.root.refreshLeft();

//        System.out.println("插入完成,当前根节点为:");
//        for(int j = 0; j < this.root.number; j++) {
//            System.out.print((V) this.root.keys[j] + " ");
//        }
//        System.out.println();
    }


    /**
     * 节点父类，因为在B+树中，非叶子节点不用存储具体的数据，只需要把索引作为键就可以了
     * 所以叶子节点和非叶子节点的类不太一样，但是又会公用一些方法，所以用Node类作为父类,
     * 而且因为要互相调用一些公有方法，所以使用抽象类
     *
     * @param <T> 同BPlusTreePlanter
     * @param <V>
     */
    abstract class Node<T, V extends Comparable<V>>{
        //父节点
        protected Node<T, V> parent;
        //子节点
        protected Node<T, V>[] childs;
        //键（子节点）数量
        protected Integer number;
        //键
        protected Object[] keys;

        //判断是否是叶结点
        protected  boolean isLeaf;

        //key对应的value值
        protected List<List<T>> values;
//        protected Object[] values;

        protected Node<T,V> left;
        protected Node<T,V> right;

        //构造方法
        public Node(){
            this.keys = new Object[maxNumber];
            this.values=new ArrayList<>();
            for(int len=0;len<maxNumber;len++){
                this.values.add(new ArrayList<>());
            }
            this.childs = new Node[maxNumber];
            this.number = 0;
            this.parent = null;
//            this.left=null;
//            this.right=null;
        }
        //查找
        abstract List<T> find(V key);
        //范围查询的边界子节点定位
        abstract List findboundary(Integer des, V key);
        //范围查询
        abstract List<T> rangefind(V min, V max);

        //插入
        abstract Node<T, V> insert(T value, V key);

        abstract LeafNode<T, V> refreshLeft();

    }

    /**
     * 非叶节点类
     * @param <T>
     * @param <V>
     */

    class BPlusNode <T, V extends Comparable<V>> extends Node<T, V>{

        public BPlusNode() {
            super();
            this.isLeaf=false;
        }

        /**
         * 递归查找,这里只是为了确定值究竟在哪一块,真正的查找到叶子节点才会查
         * @param key
         * @return
         */
        @Override
        List<T> find(V key) {
            int i = 0;
            while(i < this.number){
                if(key.compareTo((V) this.keys[i]) <= 0){
                    break;
                }
                i++;
            }
//            目标key值比最大值都大，找不到相应值
            if(this.number==i)
                return null;
            List<T> ret;
            ret=this.childs[i].find(key);
            return ret;
        }

        @Override
        List findboundary(Integer des, V key) {
            int i = 0;
            while(i < this.number){
                if(key.compareTo((V) this.keys[i]) <= 0)
                    break;
                i++;
            }
            if(i==this.number)return this.childs[i-1].findboundary(des,key);
//            目标key值比最大值都大，找不到相应值,就对最大值进行寻找
            return this.childs[i].findboundary(des,key);
        }

        @Override
        List<T> rangefind(V min, V max) {
            return null;
        }

        /**
         * 递归插入,先把值插入到对应的叶子节点,最终将调用叶子节点的插入类
         * @param value
         * @param key
         */
        @Override
        Node<T, V> insert(T value, V key) {
            int i = 0;
            while(i < this.number){
                if(key.compareTo((V) this.keys[i]) <= 0)
                    break;
                i++;
            }
            if(key.compareTo((V) this.keys[this.number - 1]) > 0) {
                i--;
//                if(this.childs[i].number + 1 <= bTreeOrder) {
//                    this.keys[this.number - 1] = key;
//                }
            }

//            System.out.println("非叶子节点查找key: " + this.keys[i]);

            return this.childs[i].insert(value, key);
        }

        @Override
        LeafNode<T, V> refreshLeft() {
            return this.childs[0].refreshLeft();
        }

        Node<T,V> refreshBoundary(Node<T, V> node){
            Node<T,V> chi =new BPlusNode<>() ;
            if (node.parent!=null)
                chi=node.childs[node.number-1];
                node.keys[node.number-1]=chi.keys[chi.number-1];
            return refreshBoundary(node.parent);
        }
        /**
         * 当叶子节点插入成功完成分解时,递归地向父节点插入新的节点以保持平衡
         * @param node1
         * @param node2
         * @param key
         */
//        将子节点放入父节点中，
        Node<T, V> insertNode(Node<T, V> node1, Node<T, V> node2, V key){

//            System.out.println("非叶子节点,插入key: " + node1.keys[node1.number - 1] + " " + node2.keys[node2.number - 1]);

            V oldKey = null;
            if(this.number > 0)
                oldKey = (V) this.keys[this.number - 1];
            //如果原有key为null,说明这个非节点是空的,直接放入两个节点即可
            if(key == null || this.number <= 0){
//                System.out.println("非叶子节点,插入key: " + node1.keys[node1.number - 1] + " " + node2.keys[node2.number - 1] + "直接插入");
                this.keys[0] = node1.keys[node1.number - 1];
                this.keys[1] = node2.keys[node2.number - 1];
                this.childs[0] = node1;
                this.childs[1] = node2;
                this.number += 2;
                return this;
            }
            //原有节点不为空,则应该先寻找原有节点的位置,然后将新的节点插入到原有节点中
            int i = 0;
            while(key.compareTo((V)this.keys[i]) != 0){
                i++;
            }
            //左边节点的最大值可以直接插入,右边的要挪一挪再进行插入
            this.keys[i] = node1.keys[node1.number - 1];
            this.childs[i] = node1;

            Object tempKeys[] = new Object[maxNumber];
            Object tempChilds[] = new Node[maxNumber];

            System.arraycopy(this.keys, 0, tempKeys, 0, i + 1);
            System.arraycopy(this.childs, 0, tempChilds, 0, i + 1);
            System.arraycopy(this.keys, i + 1, tempKeys, i + 2, this.number - i - 1);
            System.arraycopy(this.childs, i + 1, tempChilds, i + 2, this.number - i - 1);
            tempKeys[i + 1] = node2.keys[node2.number - 1];
            tempChilds[i + 1] = node2;

            this.number++;

            //判断是否需要拆分
            //如果不需要拆分,把数组复制回去,直接返回
            if(this.number <= bTreeOrder){
                System.arraycopy(tempKeys, 0, this.keys, 0, this.number);
                System.arraycopy(tempChilds, 0, this.childs, 0, this.number);

//                System.out.println("非叶子节点,插入key: " + node1.keys[node1.number - 1] + " " + node2.keys[node2.number - 1] + ", 不需要拆分");
//                return refreshBoundary(this);
                //有可能虽然没有节点分裂，但是实际上插入的值大于了原来的最大值，所以所有父节点的边界值都要进行更新
                Node node = this;
                while (node.parent != null){
                    V tempkey = (V)node.keys[node.number - 1];
                    if(tempkey.compareTo((V)node.parent.keys[node.parent.number - 1]) > 0){
                        node.parent.keys[node.parent.number - 1] = tempkey;
                        node = node.parent;
                    }
                    else {
                        break;
                    }
                }
                return null;
            }

//            System.out.println("非叶子节点,插入key: " + node1.keys[node1.number - 1] + " " + node2.keys[node2.number - 1] + ",需要拆分");

            //如果需要拆分,和拆叶子节点时类似,从中间拆开
            Integer middle = this.number / 2;

            //新建非叶子节点,作为拆分的右半部分
            BPlusNode<T, V> tempNode = new BPlusNode<T, V>();
            //非叶节点拆分后应该将其子节点的父节点指针更新为正确的指针
            tempNode.number = this.number - middle;
            tempNode.parent = this.parent;
            //如果父节点为空,则新建一个非叶子节点作为父节点,并且让拆分成功的两个非叶子节点的指针指向父节点
            if(this.parent == null) {

//                System.out.println("非叶子节点,插入key: " + node1.keys[node1.number - 1] + " " + node2.keys[node2.number - 1] + ",新建父节点");

                BPlusNode<T, V> tempBPlusNode = new BPlusNode<>();
                tempNode.parent = tempBPlusNode;
                this.parent = tempBPlusNode;
                oldKey = null;
            }
            System.arraycopy(tempKeys, middle, tempNode.keys, 0, tempNode.number);
            System.arraycopy(tempChilds, middle, tempNode.childs, 0, tempNode.number);
            for(int j = 0; j < tempNode.number; j++){
                tempNode.childs[j].parent = tempNode;
            }
            //让原有非叶子节点作为左边节点
            this.number = middle;
            this.keys = new Object[maxNumber];
            this.childs = new Node[maxNumber];
            System.arraycopy(tempKeys, 0, this.keys, 0, middle);
            System.arraycopy(tempChilds, 0, this.childs, 0, middle);

            //叶子节点拆分成功后,需要把新生成的节点插入父节点
            BPlusNode<T, V> parentNode = (BPlusNode<T, V>)this.parent;
            return parentNode.insertNode(this, tempNode, oldKey);
        }

    }

    /**
     * 叶节点类
     * @param <T>
     * @param <V>
     */
    class LeafNode <T, V extends Comparable<V>> extends Node<T, V> {
        public LeafNode(){
            super();
            this.isLeaf=true;
            this.left=null;
            this.right=null;
        }
        /**
         * 进行查找,经典二分查找,不多加注释
         * @param key
         * @return
         */
        @Override
        List<T> find(V key) {
            if(this.number <=0)
                return null;
            Integer left = 0;
            Integer right = this.number;

            Integer middle = (left + right) / 2;
            while(left < right){
                V middleKey = (V) this.keys[middle];
                if(key.compareTo(middleKey) == 0){
                    return this.values.get(middle);
                }
                else if(key.compareTo(middleKey) < 0)
                    right = middle;
                else
                    left = middle+1;
                middle = (left + right) / 2;
            }
            return null;
        }

        @Override
        //返回符合条件的左边界或者右边界所在的节点指针和节点内index，如果没有找到等值的key，则左边界返回第一个比它大的值，右边界返回第一个比它小的值
        List findboundary(Integer des, V key) {
            if(this.number <=0)
                return null;
            Integer left = 0;
            Integer right = this.number;

            Integer middle = (left + right) / 2;
            List ret;
            while(left < right){
                V middleKey = (V) this.keys[middle];
                if(key.compareTo(middleKey) == 0){
                     ret= Arrays.asList(this,middle);
                    return ret;
                }
                //目标值比middle小
                else if(key.compareTo(middleKey) < 0)
                    right = middle;
                //目标值比middle大
                else
                    left = middle+1;
                middle = (left + right) / 2;
            }
            //找左边界
            if(des==0)
                ret= Arrays.asList(this,right);
            //找右边界
            else ret= Arrays.asList(this,left-1);
            return ret;
        }

        @Override
        List<T> rangefind(V min, V max) {
            return null;
        }

        /**
         *
         * @param value
         * @param key
         */
        @Override
        Node<T, V> insert(T value, V key) {
//            System.out.println("叶子节点,插入key: " + key);
            //保存原始存在父节点的key值
            V oldKey = null;
            if(this.number > 0)
                oldKey = (V) this.keys[this.number - 1];
            //先插入数据
            int i = 0;
            while(i < this.number){
                if(key.compareTo((V) this.keys[i]) <= 0)
                    break;
                i++;
            }
            //如果插入值与节点i处的值重复
            while(i<this.number){
                if(key.compareTo((V) this.keys[i]) == 0){
                    this.values.get(i).add(value);
                    return null;
                }else break;
            }
            //复制数组,完成添加
            Object tempKeys[] = new Object[maxNumber];
            List<List<T>> tempValues= new ArrayList<>();
            for(int len=0;len<maxNumber;len++){
                tempValues.add(new ArrayList<>());
            }
            //把i之前的值复制到临时数组里
            System.arraycopy(this.keys, 0, tempKeys, 0, i);
            for(int v=0;v<i;v++){
                tempValues.set(v,this.values.get(v));
            }
//            System.arraycopy(this.values.toArray(), 0, tempValues, 0, i);
            //把i以及i后面的值复制到临时数组里
            System.arraycopy(this.keys, i, tempKeys, i + 1, this.number - i);
            for(int v=i;v<this.number;v++){
                tempValues.set(v+1,this.values.get(v));
            }
//            System.arraycopy(this.values.toArray(), i, tempValues, i + 1, this.number - i);
            tempKeys[i] = key;
            List<T> valueList=new ArrayList<>();
            valueList.add(value);
            tempValues.set(i,valueList);
            this.number++;

//            System.out.println("插入完成,当前节点key为:");
//            for(int j = 0; j < this.number; j++)
//                System.out.print(tempKeys[j] + " ");
//            System.out.println();

            //判断是否需要拆分
            //如果不需要拆分完成复制后直接返回
            if(this.number <= bTreeOrder){
//                Object tempValues2[] = new Object[maxNumber];
                System.arraycopy(tempKeys, 0, this.keys, 0, this.number);
//                System.arraycopy(tempValues, 0, this.values, 0, this.number);
                for(int t=0;t<this.number;t++){
                    List<T> test=tempValues.get(t);
                    if(this.values.size()==t){
                        this.values.add(test);
                    }else this.values.set(t,tempValues.get(t));
                }
                //有可能虽然没有节点分裂，但是实际上插入的值大于了原来的最大值，所以所有父节点的边界值都要进行更新
                Node node = this;
                while (node.parent != null){
                    V tempkey = (V)node.keys[node.number - 1];
                    if(tempkey.compareTo((V)node.parent.keys[node.parent.number - 1]) > 0){
                        node.parent.keys[node.parent.number - 1] = tempkey;
                        node = node.parent;
                    }
                    else {
                        break;
                    }
                }
//                System.out.println("叶子节点,插入key: " + key + ",不需要拆分");

                return null;
            }

//            System.out.println("叶子节点,插入key: " + key + ",需要拆分");

            //如果需要拆分,则从中间把节点拆分差不多的两部分
            Integer middle = this.number / 2;

            //新建叶子节点,作为拆分的右半部分
            LeafNode<T, V> tempNode = new LeafNode<T, V>();
            tempNode.number = this.number - middle;
            tempNode.parent = this.parent;
            //如果父节点为空,则新建一个非叶子节点作为父节点,并且让拆分成功的两个叶子节点的指针指向父节点,因为拆分前后产生的两个叶子节点的父节点为同一个
            if(this.parent == null) {

//                System.out.println("叶子节点,插入key: " + key + ",父节点为空 新建父节点");

                BPlusNode<T, V> tempBPlusNode = new BPlusNode<>();
//                叶结点的parent指针指向父节点
                tempNode.parent = tempBPlusNode;
                this.parent = tempBPlusNode;
                oldKey = null;
            }
//            给拆分的右半部分节点分配好对应的keys和values
            System.arraycopy(tempKeys, middle, tempNode.keys, 0, tempNode.number);
            Integer index=middle;
            for(int colen=0;colen<tempNode.number;colen++){
                tempNode.values.set(colen,tempValues.get(index));
                index++;
            }
//            System.arraycopy(tempValues, middle, tempNode.values, 0, tempNode.number);

            //让原有叶子节点作为拆分的左半部分
            this.number = middle;
            this.keys = new Object[maxNumber];
            this.values = new ArrayList<>();
            System.arraycopy(tempKeys, 0, this.keys, 0, middle);
            for(int colen2=0;colen2<middle;colen2++){
                this.values.add(tempValues.get(colen2));
            }
//            System.arraycopy(tempValues, 0, this.values, 0, middle);
//让拆分后的左边叶子的right指针指向拆分后的右边，右边叶子的左边指针指向左边叶子
            this.right = tempNode;
            tempNode.left = this;

            //叶子节点拆分成功后,需要把新生成的节点插入父节点，父节点中的chirld指针要指向子节点
            BPlusNode<T, V> parentNode = (BPlusNode<T, V>)this.parent;
            return parentNode.insertNode(this, tempNode, oldKey);
        }

        @Override
        LeafNode<T, V> refreshLeft() {
            if(this.number <= 0)
                return null;
            return this;
        }
    }

    public String serialize(BPlusTreePlanter tree){
        
        Node root=tree.root;
        if(root==null)return "";
        Integer bTreeOrder=tree.bTreeOrder;
        List<String> list = new LinkedList<>();
        buildString(root, list,root.values);
        list.add(bTreeOrder.toString());
        String res= String.join("~", list);
        return res;
    }
    public void buildString(Node root, List<String> list, List<List<T>> val) {
        if (root == null) return;
        list.add(String.valueOf(root.isLeaf));//标识是否是节点类型
        list.add(String.valueOf(root.number));  //添加节点的key数目
        for (int i=0;i<root.number;i++){//添加节点值
            list.add(root.keys[i].toString());
            if(root.isLeaf){
                int val_len=val.get(i).size();
                //values中有多个value，即一个key对应多个value
                //把各个key拥有的value数量加入字符串
                list.add(String.valueOf(val_len));
                String json;
                for(int j=0;j<val_len;j++){
                    json= JSON.toJSON(val.get(i).get(j)).toString();
                    list.add(json);
                }
            }

        }
        if(root.isLeaf)list.add("-1");//叶子节点没有孩子，孩子数量置为-1
        else {
            list.add(String.valueOf(root.number));  //添加节点的孩子数目
            for (int c_index=0;c_index<root.number;c_index++) {
                buildString(root.childs[c_index], list,root.childs[c_index].values);
            }
        }

    }

    //反序列化
    public BPlusTreePlanter deserialize(String data){
        if(data.length() == 0) return null;
        String[] str = data.split("~");
        Queue<String> queue = new LinkedList<>();
        Collections.addAll(queue, str);
        Node root=buildTree(null,queue);
        Node temp=root;
        while (!temp.isLeaf){
            temp=temp.childs[0];
        }
        Integer bTreeOrder= Integer.parseInt(queue.poll());
        BPlusTreePlanter<T, Integer> tree=new BPlusTreePlanter<>(bTreeOrder,root);
        tree.left=(LeafNode)temp;
        return tree;
    }

    public Node buildTree(Node parent, Queue<String> queue){
        Boolean isLeaf;
        Node node;
        if(!Boolean.parseBoolean(queue.poll())){
            isLeaf=false;
            node = new BPlusNode();

        }else {
            isLeaf=true;
            node = new LeafNode();
        }
        node.parent=parent;
        //父节点填满key值
        Integer key_num= Integer.parseInt(queue.poll());
        //父节点的number值填满
        node.number=key_num;
        //节点keys和对应values集合填满
        List<List<T>> temp=new ArrayList<>();
        for(int i=0;i<key_num;i++){
            node.keys[i] = Integer.parseInt(queue.poll());
            if(isLeaf){//为叶子借点填充values
                int val_len= Integer.parseInt(queue.poll());//key有几个values
                List<T> list=new ArrayList<>();
                for(int j=0;j<val_len;j++){
                    T product= (T) JSON.parseObject(queue.poll(), PlanterTransient.class);
                    list.add(product);
                }
                temp.add(list);
            }
        }
        node.values=temp;
        int size = Integer.parseInt(queue.poll());//孩子数量
        for(int c=0;c<size;c++){
            node.childs[c] = buildTree(node,queue);
        }
        for(int in=0;in<size;in++){
            //节点的左右指针，如果有就传入，没有就为空节点
            Node<T,V> left;
            Node<T,V> right;
            if(in>0){
                left=node.childs[in-1];
                node.childs[in].left=left;
            }
            if(in<size-1){
                right=node.childs[in+1];
                node.childs[in].right=right;
            };
        }

        return node;
    }
}


