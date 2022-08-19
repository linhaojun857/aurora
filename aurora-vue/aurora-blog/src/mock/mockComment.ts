export default {
  recentComments: [
    {
      id: 1,
      user: {
        avatar: 'https://img-blog.csdnimg.cn/20210313122054101.png',
        username: '人物1'
      },
      is_admin: true,
      created_at: 'commented at 2 months ago',
      body: 'this is test comment'
    }
  ],
  comments: [
    {
      id: 1,
      username: '人物1',
      content: '我叫人物1,这时mock数据,我叫人物1,这时mock数据,我叫人物1,这时mock数据,我叫人物1,这时mock数据,我叫人物1,这时mock数据,我叫人物1,这时mock数据',
      time: '2021-05-02T15:19:24.000Z',
      replies: [
        {
          id: 3,
          username: '人物3',
          content: '我叫人物3,这时mock数据',
          time: '2021-05-02T15:19:24.000Z'
        },
        {
          id: 4,
          username: '人物4',
          content: '我叫人物4,这时mock数据',
          time: '2021-05-02T15:19:24.000Z'
        }
      ]
    },
    {
      id: 2,
      username: '人物2',
      content: '我叫人物2,这时mock数据',
      time: '2021-05-02T15:19:24.000Z',
      replies: []
    }
  ]
}
