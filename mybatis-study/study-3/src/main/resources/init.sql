Create TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(50) ,
  `userAge` int(11) ,
  `userAddress` varchar(200),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

Insert INTO `user` VALUES ('1', 'wangji', '25', 'guizhou');



Drop TABLE IF EXISTS `article`;
Create TABLE `article` (
  `id` int(11) NOT NULL auto_increment,
  `userid` int(11) NOT NULL,
  `title` varchar(100) NOT NULL,
  `content` text NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;


Insert INTO `article` VALUES ('1', '1', '计算机学习', '计算机');
Insert INTO `article` VALUES ('2', '1', 'CSS学习', 'CSS学习');
Insert INTO `article` VALUES ('3', '1', 'Java ORM Study', 'Java ORM Study');
