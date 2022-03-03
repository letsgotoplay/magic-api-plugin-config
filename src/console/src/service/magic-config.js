export default function (bus, constants, $i, Message, request) {
    return {
        // svg text
        getIcon: item => ['CONF', '#00BFFF'],
        // 任务名称
        name: $i('config.name'),
        language: 'yaml',
        // 执行测试的逻辑
        // 是否允许执行测试
        runnable: false,
        // 是否需要填写路径
        requirePath: false,
        // 合并
        merge: item => item
    }
}
