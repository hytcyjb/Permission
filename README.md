  （声明：本文基于https://github.com/hotchemi/PermissionsDispatcher 源码进行修改的，谢谢原创者）
  
  android 系统6.0之后的权限需要申请，这个封装的很好，详细的讲解了4个步骤：
   
   
    //1---1.如果权限申请成功就走这里，同时也是操作这里的时候请求的
    private void showCamera() {
        show("11---相机有了权限了");
    }
    
    
    //2.2.拒绝之后，再次请求，小米不会调用这里，华为可以走这里，这是弹出一个弹窗，然后可以允许，
    允许之后就再次跳出系统的请求权限的对话框
    private void showRationaleForCamera(PermissionRequest request) {
        showRationaleDialog(R.string.requset_camear, request);
        show("22====相机请求权限对话框");
    }
    
    
    //3.拒绝之后调用：（小米的直接走这里不走第2步，华为的可以先走2，）
    private void onCameraDenied() {
        show("33相机启动失败");
    }
    
    
    //4.拒绝之后，再次请求，小米不会调用这里，华为可以走这里
    private void onCameraNeverAskAgain() {
        show("44相机再次请求，弹出对话框");
    }
    
    
    
