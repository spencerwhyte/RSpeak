//
//  AskQuestionDelegate.h
//  IOSAnonQuestion
//
//  Created by Spencer Whyte on 2014-03-15.
//  Copyright (c) 2014 Spencer Whyte. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Question.h"

@protocol AskQuestionDelegate <NSObject>

-(void)questionAskDidFail;
-(void)questionAskDidSucceed:(Question*)question;


@end