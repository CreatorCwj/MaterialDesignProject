package com.design_pattern;

import android.support.annotation.NonNull;

/**
 * Created by cwj on 17/5/14.
 * 责任链模式
 */

public class ResponsibilityLink {

    public static void main(String[] args) {
        //FileItem
        FileItem rootItem = new FileItem(FileType.ROOT);
        FileItem folderItem = new FileItem(FileType.FOLDER);
        FileItem fileItem = new FileItem(FileType.FILE);

        //First Handler
        FileItemHandler handler = FileItemHandler.getNormalHandler();

        //handle
        String result = handler.handle(rootItem);
        System.out.println(result);
        result = handler.handle(folderItem);
        System.out.println(result);
        result = handler.handle(fileItem);
        System.out.println(result);
    }
}

enum FileType {
    ROOT("根目录"),
    FOLDER("文件夹"),
    FILE("文件");

    private final String desc;

    FileType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}

class FileItem {

    private final FileType fileType;

    FileItem(FileType fileType) {
        this.fileType = fileType;
    }

    public FileType getFileType() {
        return fileType;
    }
}

abstract class FileItemHandler {

    public static FileItemHandler getNormalHandler() {
        FileItemHandler firstHandler = new RootHandler();
        firstHandler.setNextHandler(new FolderHandler());
        return firstHandler;
    }

    private FileItemHandler nextHandler;

    public void setNextHandler(FileItemHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public final String handle(@NonNull FileItem fileItem) {
        if (fileItem.getFileType() == getHandleType()) {//当前节点处理
            return handleFileItem(fileItem);
        } else if (nextHandler != null) {
            return nextHandler.handle(fileItem);
        } else {
            return fileItem.getFileType().getDesc() + "类型无节点可以处理";
        }
    }

    protected abstract FileType getHandleType();

    protected abstract String handleFileItem(FileItem fileItem);
}

class RootHandler extends FileItemHandler {
    @Override
    protected FileType getHandleType() {
        return FileType.ROOT;
    }

    @Override
    protected String handleFileItem(FileItem fileItem) {
        return "根节点处理器：" + fileItem.getFileType().getDesc();
    }
}

class FolderHandler extends FileItemHandler {

    @Override
    protected FileType getHandleType() {
        return FileType.FOLDER;
    }

    @Override
    protected String handleFileItem(FileItem fileItem) {
        return "文件夹节点处理器：" + fileItem.getFileType().getDesc();
    }
}
