package com.jd.o2o.vipcart.service.common.busi.pattern.impl;

import com.jd.o2o.vipcart.common.domain.exception.BaseMsgException;
import com.jd.o2o.vipcart.common.plugins.log.track.LoggerTrackFactory;
import com.jd.o2o.vipcart.common.utils.json.JsonUtils;
import com.jd.o2o.vipcart.service.common.busi.pattern.Command;
import com.jd.o2o.vipcart.service.common.busi.pattern.GroupCommand;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;

import java.util.List;

/**
 * 命令列表执行模板方法
 * Created by liuhuiqing on 2017/6/12.
 */
public abstract class GroupCommandTemplate<T> implements GroupCommand<T> {
    private static final Logger LOGGER = LoggerTrackFactory.getLogger(GroupCommandTemplate.class);
    private String description;
    private List<? extends Command> commandList;

    public GroupCommandTemplate() {
    }

    public GroupCommandTemplate(String description, List<? extends Command> commandList) {
        this.description = description;
        this.commandList = commandList;
    }

    public void execute(T commandParam) {
        if (CollectionUtils.isEmpty(commandList)) {
            return;
        }
        int n = 0;
        boolean hasException = false;
        for (Command command : commandList) {
            try {
                command.handler(commandParam);
                n++;
            } catch (Throwable e) {
                hasException = true;
                LOGGER.error(String.format("[%s]出现异常,将进行回滚操作,class=[%s],param=[%s]",
                        description, command.getClass().getName(), JsonUtils.toJson(commandParam)), e);
                break;
            }
        }
        if (hasException) {
            rollBackUservipcartCommand(n, commandParam);
        }
        try {
            afterCommand(commandParam);
        } catch (Exception e) {
            LOGGER.error(String.format("[%s]在执行业务逻辑时出现异常", description), e);
            rollBackUservipcartCommand(n, commandParam);
        }
    }

    /**
     * 回滚优惠券变更同步数据流程
     *
     * @param endIndex
     * @param commandParam
     */
    private void rollBackUservipcartCommand(int endIndex, T commandParam) {
        try {
            if (CollectionUtils.isEmpty(commandList)) {
                return;
            }
            if (endIndex > commandList.size()) {
                throw new BaseMsgException("找不到给定回滚命令的下标实现！");
            }
            for (int i = 0; i < endIndex; i++) {
                Command command = commandList.get(i);
                command.rollBack(commandParam);
            }
        } catch (Throwable e) {
            LOGGER.error(String.format("[%s]回滚逻辑出现异常,param=[%s]", description, JsonUtils.toJson(commandParam)), e);
        } finally {
            // 抛出异常进行重试
            throw new BaseMsgException(String.format("[%s]出现异常,param=[%s]", description, JsonUtils.toJson(commandParam)));
        }
    }

    public GroupCommandTemplate buildDescription(String description) {
        this.description = description;
        return this;
    }

    public GroupCommandTemplate buildCommandList(List<? extends Command> commandList) {
        this.commandList = commandList;
        return this;
    }
}
